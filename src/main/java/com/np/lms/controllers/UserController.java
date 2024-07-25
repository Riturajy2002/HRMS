package com.np.lms.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.np.lms.DTO.UserInfo;
import com.np.lms.DTO.UserLoginInfo;
import com.np.lms.auth.Secured;
import com.np.lms.entities.User;
import com.np.lms.enums.Role;
import com.np.lms.repositories.SqlDAO;
import com.np.lms.repositories.UserRepository;
import com.np.lms.services.UserServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserServices userService;

	@Autowired
	private LoginRestController loginRestController;

	@Autowired
	private HttpServletRequest context;

	@Autowired
	private UserRepository userRepository;

	private UserInfo getUser() {
		String authToken = "";
		if (context != null) {
			authToken = context.getHeader("auth-token");
		}
		return loginRestController.getLoggedUser(authToken);
	}

	// Just for Refresh Purpose when entry done in DB directly then calling from Postman.
	@GetMapping(value = "/refreshUsers")
	public String refreshUsers() {
		loginRestController.refreshUsers();
		return "Success";
	}

	// For login the user with their credentials.
	@PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
	public UserLoginInfo login(@RequestBody UserInfo credentials) {
		return loginRestController.login(credentials);
	}

	// For logged out the user by getting their auth-token and removing it from the HashMap.
	@GetMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		String authToken = request.getHeader("auth-token");
		if (authToken != null && !authToken.isEmpty()) {
			String logoutResult = loginRestController.logout(authToken);
			if (logoutResult.equals("Success")) {
				return ResponseEntity.ok().body("{\"message\": \"Logged out successfully\"}");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Logout failed\"}");
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"Unauthorized\"}");
		}
	}

	// Finding the user all information for just showing it.
	@GetMapping("/{id}")
	public ResponseEntity<Optional<User>> getUserById(@PathVariable String id) {
		Optional<User> user = userService.getUserById(id);
		if (user != null) {
			return ResponseEntity.ok(user);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// For checking that if user is already registered from the same name, Email or
	// contact from the admin page.
	@PostMapping("/checkUserExists")
	@Secured({ Role.Admin })
	public ResponseEntity<Map<String, Boolean>> checkUserExists(@RequestBody User user) {
		Map<String, Boolean> response = new HashMap<>();
		User existingUser = userService.findByUsername(user.getUsername());
		response.put("usernameExists", existingUser != null);
		response.put("emailExists", userService.findByEmailId(user.getEmailId()).isPresent());
		response.put("contactNoExists", userService.findByContactNo(user.getContactNo()).isPresent());
		return ResponseEntity.ok(response);
	}

	// Uploading a Profile Picture
	private static final String UPLOAD_DIR = "src/main/resources/static/img/";

	@PostMapping("/uploadProfilePic/{id}")
	@Secured({ Role.Admin, Role.Manager, Role.User })
	public ResponseEntity<?> uploadProfilePic(@PathVariable String id, @RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("File is empty.");
		}

		long maxFileSize = 16 * 1024 * 1024; // 16MB in bytes
		if (file.getSize() > maxFileSize) {
			return ResponseEntity.badRequest().body("File size exceeds the maximum allowed size of 16MB.");
		}

		List<String> allowedTypes = Arrays.asList("image/jpeg", "image/png", "image/gif");
		if (!allowedTypes.contains(file.getContentType())) {
			return ResponseEntity.badRequest().body("Only image files (JPEG, PNG, GIF) are allowed.");
		}

		Optional<User> userOptional = userService.getUserById(id);
		if (userOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		User user = userOptional.get();

		try {
			// Delete existing profile picture if it exists
			if (user.getProfilePicUrl() != null && !user.getProfilePicUrl().isEmpty()) {
				Path existingFilePath = Paths.get(UPLOAD_DIR + user.getProfilePicUrl());
				Files.deleteIfExists(existingFilePath);
			}

			// Generate unique filename or use original filename
			String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
			Path filePath = Paths.get(UPLOAD_DIR + fileName);

			// Save file to the server
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			  // Update user profile picture URL in database using the custom query
	        userRepository.updateProfilePicUrl(id, fileName);
			

			return ResponseEntity.ok().body(Collections.singletonMap("profilePicUrl", fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture.");
		}
	}

	// Fetching the profile pic to show on the dashboards.
	@GetMapping(value = "/profilePic/{id}", produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	@Secured({ Role.Admin, Role.User, Role.Manager })
	public ResponseEntity<byte[]> getProfilePic(@PathVariable String id,
			@RequestHeader("auth-token") String authToken) {
		try {
			// Validate auth token and retrieve user as before
			Optional<User> userOptional = userService.getUserById(id);
			if (userOptional.isPresent()) {
				User user = userOptional.get();

				if (user.getProfilePicUrl() == null || user.getProfilePicUrl().isEmpty()) {
					// Handle case where profile picture URL is not set
					return ResponseEntity.notFound().build();
				}

				// Construct file path from stored URL
				Path filePath = Paths.get(UPLOAD_DIR + user.getProfilePicUrl());

				// Read file bytes and return as response
				byte[] imageBytes = Files.readAllBytes(filePath);

				// Determine content type based on file extension
				MediaType contentType = MediaType.IMAGE_JPEG;
				if (user.getProfilePicUrl().endsWith(".png")) {
					contentType = MediaType.IMAGE_PNG;
				} else if (user.getProfilePicUrl().endsWith(".gif")) {
					contentType = MediaType.IMAGE_GIF;
				}

				return ResponseEntity.ok().contentType(contentType).contentLength(imageBytes.length).body(imageBytes);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}