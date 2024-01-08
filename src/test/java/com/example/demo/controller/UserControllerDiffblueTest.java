package com.example.demo.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.demo.config.UserAuthenticationProvider;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerDiffblueTest {
    @MockBean
    private UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link UserController#updateUser(User)}
     */
    @Test
    void testUpdateUser() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setMobileNumber("42");
        user.setPassword("iloveyou");
        user.setToken("ABC123");
        user.setUsername("janedoe");
        when(userService.updateUser(Mockito.<User>any())).thenReturn(user);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setMobileNumber("42");
        user2.setPassword("iloveyou");
        user2.setToken("ABC123");
        user2.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/updateUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"mobileNumber\":\"42\",\"password\":\"iloveyou"
                                        + "\",\"token\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(String)}
     */
    @Test
    void testDeleteUser() throws Exception {
        // Arrange
        when(userService.deleteUser(Mockito.<String>any())).thenReturn("Delete User");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/deleteUser/{email}",
                "jane.doe@example.org");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Delete User"));
    }

    /**
     * Method under test: {@link UserController#exportAllUsersToExcel()}
     */
    @Test
    void testExportAllUsersToExcel() throws Exception {
        // Arrange
        doNothing().when(userService).exportAllUsersToExcel();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/exportAllUsersToExcel");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(
                        MockMvcResultMatchers.content().string("Export initiated. Check the specified folder for the Excel file."));
    }

    /**
     * Method under test: {@link UserController#exportAllUsersToExcel()}
     */
    @Test
    void testExportAllUsersToExcel2() throws Exception {
        // Arrange
        doNothing().when(userService).exportAllUsersToExcel();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/exportAllUsersToExcel");
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(
                        MockMvcResultMatchers.content().string("Export initiated. Check the specified folder for the Excel file."));
    }

    /**
     * Method under test: {@link UserController#updateUsersInExcel(List)}
     */
    @Test
    void testUpdateUsersInExcel() throws Exception {
        // Arrange
        doNothing().when(userService).updateUsersInExcel(Mockito.<List<User>>any(), Mockito.<String>any());

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setMobileNumber("42");
        user.setPassword("iloveyou");
        user.setToken("ABC123");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        String content = (new ObjectMapper()).writeValueAsString(userList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/updateUsersInExcel")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Excel file updated successfully."));
    }

    /**
     * Method under test: {@link UserController#getAllUsers()}
     */
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange
        when(userService.getAllUser()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/getAllUsers");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link UserController#login(User)}
     */
    @Test
    void testLogin() throws Exception {
        // Arrange
        when(userAuthenticationProvider.createToken(Mockito.<String>any())).thenReturn("ABC123");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setMobileNumber("42");
        user.setPassword("iloveyou");
        user.setToken("ABC123");
        user.setUsername("janedoe");
        when(userService.loginUser(Mockito.<User>any())).thenReturn(user);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setMobileNumber("42");
        user2.setPassword("iloveyou");
        user2.setToken("ABC123");
        user2.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"mobileNumber\":\"42\",\"password\":\"iloveyou"
                                        + "\",\"token\":\"ABC123\"}"));
    }

    /**
     * Method under test: {@link UserController#registerUser(User)}
     */
    @Test
    void testRegisterUser() throws Exception {
        // Arrange
        when(userAuthenticationProvider.createToken(Mockito.<String>any())).thenReturn("ABC123");

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setMobileNumber("42");
        user.setPassword("iloveyou");
        user.setToken("ABC123");
        user.setUsername("janedoe");
        when(userService.addUser(Mockito.<User>any())).thenReturn(user);

        User user2 = new User();
        user2.setEmail("jane.doe@example.org");
        user2.setId(1);
        user2.setMobileNumber("42");
        user2.setPassword("iloveyou");
        user2.setToken("ABC123");
        user2.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(user2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"username\":\"janedoe\",\"email\":\"jane.doe@example.org\",\"mobileNumber\":\"42\",\"password\":\"iloveyou"
                                        + "\",\"token\":\"ABC123\"}"));
    }
}
