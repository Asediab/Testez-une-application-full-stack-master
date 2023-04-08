package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @MockBean
    private UserService service;
    @MockBean
    private Authentication authentication;
    @Autowired
    private UserController userController;


    private final Long id = 1L;
    private final String email = "yoga@studio.com";
    private final String firstName = "Admin";
    private final String lastName = "Admin";
    private final String password = "test!1234";

    private User user = new User(id, email, lastName, firstName, password, true,LocalDateTime.now(), LocalDateTime.now());

    private User user2 = new User(1L, email, lastName, firstName, password, true,LocalDateTime.now(), LocalDateTime.now());
    private UserDetails userDetails = new UserDetailsImpl(id, email, firstName, lastName, true, password);


    @Test
    @DisplayName("Test findById")
    void testFindById() {
        doReturn(user).when(service).findById(anyLong());

        ResponseEntity<?> response = userController.findById("1");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test findById, id isn't find")
    void testFindByIdNotFound() {
        doReturn(null).when(service).findById(anyLong());

        ResponseEntity<?> response = userController.findById("1");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test findById , id isn't number")
    void testFindByIdIsNotNumber() {
        doThrow(NumberFormatException.class).when(service).findById(anyLong());
        ResponseEntity<?> response = null;

        try {
            response = userController.findById("notNumber");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test save , id isn't number")
    void testSaveIdIsNotNumber() {
        doThrow(NumberFormatException.class).when(service).findById(anyLong());
        ResponseEntity<?> response = null;

        try {
            response = userController.save("notNumber");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test save, id isn't find")
    void testSaveIdNotFound() {
        doReturn(null).when(service).findById(anyLong());

        ResponseEntity<?> response = userController.save("1");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
