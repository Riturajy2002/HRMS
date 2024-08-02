package com.np.lms.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.lms.auth.AES;
import com.np.lms.entities.User;
import com.np.lms.repositories.UserRepository;

@Service
public class UserServices {
	@Autowired
	private UserRepository userRepository;

	public Optional<User> getUserById(String id) {
		return userRepository.findById(id);
	}

	public Optional<User> findById(String userId) {
		return userRepository.findById(userId);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public Optional<User> findByEmailId(String emailId) {
		return userRepository.findByEmailId(emailId);
	}

	public Optional<User> findByContactNo(Long contactNo) {
		return userRepository.findByContactNo(contactNo);
	}
}
