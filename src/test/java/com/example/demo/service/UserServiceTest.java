package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testAddUser() {
        // Arrange
        User inputUser = new User();
        inputUser.setEmail("test@example.com");
        inputUser.setPassword("password");

        User savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword"); // Assuming this is the encoded password

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        // Act
        User resultUser = userService.addUser(inputUser);

        // Assert
        verify(passwordEncoder).encode("password");
        verify(userRepo).save(any(User.class));

        assertEquals("test@example.com", resultUser.getEmail());
        assertEquals("encodedPassword", resultUser.getPassword());
        // Add more assertions as needed
    }


    @Test
    public void testLoginUser_InvalidPassword() {
        // Arrange
        String userEmail = "test@example.com";
        String userPassword = "password123";

        User userInDatabase = new User();
        userInDatabase.setEmail(userEmail);
        userInDatabase.setPassword(passwordEncoder.encode("wrongPassword"));

        when(userRepo.findByEmail(userEmail)).thenReturn(userInDatabase);
        when(passwordEncoder.matches(userPassword, userInDatabase.getPassword())).thenReturn(false);

        // Act
        User loggedInUser = userService.loginUser(new User(userEmail, userPassword));

        // Assert
        assertNull(loggedInUser);
    }

    @Test
    public void testLoginUser_UserNotFound() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        String userPassword = "password123";

        when(userRepo.findByEmail(userEmail)).thenReturn(null);

        // Act
        User loggedInUser = userService.loginUser(new User(userEmail, userPassword));

        // Assert
        assertNull(loggedInUser);
    }
    @Test
    public void testUpdateUser() {
        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        User updatedUser = new User();
        updatedUser.setEmail("existing@example.com");
        updatedUser.setUsername("UpdatedUsername");

        when(userRepo.findByEmail("existing@example.com")).thenReturn(existingUser);
        when(userRepo.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals("UpdatedUsername", result.getUsername());
    }

    @Test
    public void testDeleteUser() {
        User userToDelete = new User();
        userToDelete.setEmail("delete@example.com");

        when(userRepo.findByEmail("delete@example.com")).thenReturn(userToDelete);

        String result = userService.deleteUser("delete@example.com");

        assertEquals("User Deleted Successfully", result);
        verify(userRepo, times(1)).delete(userToDelete);
    }

    @Test
    public void testGetAllUser() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepo.findAll()).thenReturn(userList);

        List<User> result = userService.getAllUser();

        assertEquals(2, result.size());
    }
}
