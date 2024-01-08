package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService extends ExcelService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ExcelUpdateService excelUpdateService;

	public User addUser(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

	public User loginUser(User user) {
		System.out.println(user.getPassword());
		User user1 = userRepo.findByEmail(user.getEmail());

		if (user1 != null) { // Add a null check here
			System.out.println(user1.getPassword());

			if (passwordEncoder.matches(user.getPassword(), user1.getPassword())) {
				System.out.println("checking");
				return user1;
			}
		}

		return null;
	}

	public User findByEmail(String email) {
	   return userRepo.findByEmail(email);
   }
   public User updateUser(User updatedUser) {
	    try {
	        User existingUser = userRepo.findByEmail(updatedUser.getEmail());
            if (existingUser != null) {
                // Update user fields as needed
                existingUser.setUsername(updatedUser.getUsername());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setMobileNumber(updatedUser.getMobileNumber());

                // Check if the password is not empty and encode it
                String newPassword = updatedUser.getPassword();
                if (newPassword != null && !newPassword.isEmpty()) {
                    String encodedPassword = passwordEncoder.encode(newPassword);
                    existingUser.setPassword(encodedPassword);
                }

                // Update other fields as needed

                // Save the updated user
                return userRepo.save(existingUser);
            }

        } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }

	    return null; // Handle appropriately based on your use case
	}



	public String deleteUser(String email) {
		User user1=userRepo.findByEmail(email);
		userRepo.delete(user1);
		return "User Deleted Successfully";
	}

	public List<User> getAllUser() {
		List<User> users;
        users = userRepo.findAll();
        return users;

	}

	public void exportAllUsersToExcel() {
		List<User> users = getAllUsers();
		String excelFilePath = "C:/Users/91630/Downloads/all_users_data1.xlsx";

		try {
			ExcelService.exportUsersToExcel(users, excelFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception as needed
		}
	}

	public void updateUsersInExcel(List<User> usersToUpdate, String filePath) {
		try {
			ExcelUpdateService.updateUsersInExcel(usersToUpdate, filePath);
		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception as needed
		}
	}

	private List<User> getAllUsers() {
		List<User> users;
        users = userRepo.findAll();
        return users;
	}




}
