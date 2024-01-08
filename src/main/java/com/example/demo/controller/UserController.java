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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.UserAuthenticationProvider;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@CrossOrigin("*")
@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserAuthenticationProvider userAuthenticationProvider;




	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user){

		User user1=userService.addUser(user);
		//give him a  jwt token
		user1.setToken(userAuthenticationProvider.createToken(user1.getEmail()));
		System.out.println(" jwt token whlie register "+user1.getToken());
		return ResponseEntity.ok(user1);
	}

	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user){
		System.out.println(" This method was hit ");
		User user1=userService.loginUser(user);
		//give him a  jwt token
		user1.setToken(userAuthenticationProvider.createToken(user1.getEmail()));
		System.out.println(" jwt token whlie login "+user1.getToken());
		return ResponseEntity.ok(user1);
	}

	@PostMapping("/updateUser")
    public ResponseEntity<User> updateUser( @RequestBody User updatedUser) {
        User updatedUserData = userService.updateUser(updatedUser);

        if (updatedUserData != null) {
            return new ResponseEntity<>(updatedUserData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteUser/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        String result=userService.deleteUser(email);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
    	List<User> users=userService.getAllUser();
    	return ResponseEntity.ok(users);

    }
	@GetMapping("/exportAllUsersToExcel")
	public ResponseEntity<String> exportAllUsersToExcel() {
		userService.exportAllUsersToExcel();
		return ResponseEntity.ok("Export initiated. Check the specified folder for the Excel file.");
	}
	@PostMapping("/updateUsersInExcel")
	public ResponseEntity<String> updateUsersInExcel(@RequestBody List<User> usersToUpdate) {
		String filePath = "path/to/your/existing/excel/file.xlsx"; // Specify the correct file path
		userService.updateUsersInExcel(usersToUpdate, filePath);
		return ResponseEntity.ok("Excel file updated successfully.");
	}



}
