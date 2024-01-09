package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.UserAuthenticationProvider;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@CrossOrigin("*")
@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			User registeredUser = userService.addUser(user);
			registeredUser.setToken(userAuthenticationProvider.createToken(registeredUser.getEmail()));
			logger.info("User registered successfully: {}", registeredUser.getEmail());
			return ResponseEntity.ok(registeredUser);
		} catch (Exception e) {
			logger.error("Error registering user", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error registering user: " + e.getMessage());
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
		try {
			logger.info("Login attempt for user: {}", user.getEmail());
			User loggedInUser = userService.loginUser(user);
			if (loggedInUser != null) {
				loggedInUser.setToken(userAuthenticationProvider.createToken(loggedInUser.getEmail()));
				logger.info("User logged in successfully: {}", loggedInUser.getEmail());
				return ResponseEntity.ok(loggedInUser);
			} else {
				logger.warn("Login failed for user: {}", user.getEmail());
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body("Invalid credentials. Login failed.");
			}
		} catch (Exception e) {
			logger.error("Error during login", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error during login: " + e.getMessage());
		}
	}

	@PostMapping("/updateUser")
	public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
		try {
			User updatedUserData = userService.updateUser(updatedUser);
			if (updatedUserData != null) {
				logger.info("User updated successfully: {}", updatedUserData.getEmail());
				return new ResponseEntity<>(updatedUserData, HttpStatus.OK);
			} else {
				logger.warn("User not found for update: {}", updatedUser.getEmail());
				return new ResponseEntity<>("User not found for update", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Error updating user", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating user: " + e.getMessage());
		}
	}

	@DeleteMapping("/deleteUser/{email}")
	public ResponseEntity<?> deleteUser(@PathVariable String email) {
		try {
			String result = userService.deleteUser(email);
			logger.info("User deleted successfully: {}", email);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("Error deleting user", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting user: " + e.getMessage());
		}
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<User> users = userService.getAllUser();
			logger.info("Retrieved all users");
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			logger.error("Error getting all users", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error getting all users: " + e.getMessage());
		}
	}

	@GetMapping("/exportAllUsersToExcel")
	public ResponseEntity<?> exportAllUsersToExcel() {
		try {
			userService.exportAllUsersToExcel();
			logger.info("Exported all users to Excel");
			return ResponseEntity.ok("Export initiated. Check the specified folder for the Excel file.");
		} catch (Exception e) {
			logger.error("Error exporting users to Excel", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error exporting users to Excel: " + e.getMessage());
		}
	}

	@PostMapping("/updateUsersInExcel")
	public ResponseEntity<?> updateUsersInExcel(@RequestBody List<User> usersToUpdate) {
		try {
			String filePath = "C:/Users/91630/Downloads/all_users_data1.xlsx"; // Specify the correct file path
			userService.updateUsersInExcel(usersToUpdate, filePath);
			logger.info("Updated users in Excel successfully");
			return ResponseEntity.ok("Excel file updated successfully.");
		} catch (Exception e) {
			logger.error("Error updating users in Excel", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating users in Excel: " + e.getMessage());
		}
	}
}

