package com.np.hrms.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.np.hrms.auth.AES;
import com.np.hrms.dto.UserInfo;
import com.np.hrms.dto.UserLoginInfo;
import com.np.hrms.enums.Role;
import com.np.hrms.repositories.SqlDAO;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping(path = "/security")
public class LoginRestController {
	private static final Logger LOG = LoggerFactory.getLogger(LoginRestController.class);
	private ConcurrentHashMap<String, UserInfo> users = new ConcurrentHashMap<String, UserInfo>();
	private ConcurrentHashMap<String, UserLoginInfo> sessions = new ConcurrentHashMap<String, UserLoginInfo>();

	@Autowired
	private SqlDAO sqlDao;

	@PostConstruct
	public void refreshMetaData() {
		List<UserInfo> userData = sqlDao.getUsers();
		ConcurrentHashMap<String, UserInfo> currentUsers = new ConcurrentHashMap<String, UserInfo>();
		for (UserInfo user : userData) {
			currentUsers.put(user.getUserId(), user);
		}
		users = currentUsers;
		for (String userId : currentUsers.keySet()) {
			UserInfo user = currentUsers.get(userId);

			if (StringUtils.isNotBlank(userId)) {
				String apiKey = getApiKey(user);
				if (!sessions.contains(apiKey)) {
					UserLoginInfo loginInfo = new UserLoginInfo();
					loginInfo.setUserId(userId);
					loginInfo.setAuthToken(apiKey);
					if (StringUtils.isNotBlank(user.getRole())) {
						loginInfo.setRoles(getRolesTArray(user.getRole()));
					}
					sessions.put(apiKey, loginInfo);
				}
			}
		}
	}

	void refreshUsers() {
		refreshMetaData();
	}

	public UserLoginInfo login(UserInfo credentials) {
		String userName = credentials.getUserId();
		String passWord = credentials.getPassword();
		try {
			userName = AES.decryptString(userName, "2fs6828r61oo68rr0su3eurf4serfu675eerf5oeoesr60s17fo5u5u418us5372");
			passWord = AES.decryptString(passWord, "2fs6828r61oo68rr0su3eurf4serfu675eerf5oeoesr60s17fo5u5u418us5372");

			UserInfo user = users.get(userName);
			if (user != null && user.getPassword().equals(passWord)) {
				UserLoginInfo loginInfo = new UserLoginInfo();
				loginInfo.setId(user.getId());
				loginInfo.setUserId(user.getUserId());
				loginInfo.setUserName(user.getUsername());
				loginInfo.setPassword(user.getPassword());
				loginInfo.setDesignation(user.getDesignation());
				loginInfo.setReportManager(user.getReportManager());
				loginInfo.setEmail_id(user.getEmail_id());
				loginInfo.setContactNo(user.getContactNo());
				loginInfo.setProfilePicUrl(user.getProfilePicUrl());
				loginInfo.setGender(user.getGender());
				String key = getApiKey(user);
				;
				sessions.put(key, loginInfo);
				loginInfo.setAuthToken(key);
				if (StringUtils.isNotBlank(user.getRole())) {
					loginInfo.setRoles(getRolesTArray(user.getRole()));
				}
				return loginInfo;
			}
		} catch (Exception ioException) {
			LOG.error("Error");
			System.out.println("Ërror while fetching login details");
		}
		return null;
	}

	public String logout(String key) {
		UserLoginInfo loginInfo = sessions.remove(key);
		if (loginInfo != null) {
			return "Success";
		} else {
			return "Failed";
		}
	}

	public UserLoginInfo getUserLoginInfo(String authToken) {
		UserLoginInfo user = sessions.get(authToken);
		return user;
	}

	public List<Role> getRoles(String authToken) {
		List<Role> roles = new ArrayList<Role>();
		UserLoginInfo userlogin = sessions.get(authToken);
		if (userlogin != null) {
			return userlogin.getRoles();
		}
		return roles;
	}

	public List<Role> getRolesTArray(String role) {
		List<Role> roles = new ArrayList<Role>();
		for (String r : role.split(",")) {
			roles.add(Role.valueOf(r));
		}
		return roles;
	}

	public UserInfo getLoggedUser(String authToken) {
		UserLoginInfo user = sessions.get(authToken);
		return users.get(user.getUserId());
	}

	private String getApiKey(UserInfo user) {
		return "API://" + AES.encrypt(String.format("%s-%s", user.getUserId(), user.getKey()), "bitnami@123");
	}

	public static void main(String args[]) {
		System.out.println("API://" + AES.encrypt(String.format("%s-%s", "admin", "NOVTSVFS532"), "bitnami@123"));
	}

	public UserInfo getUser(String userId) {
		return users.get(userId);
	}

}