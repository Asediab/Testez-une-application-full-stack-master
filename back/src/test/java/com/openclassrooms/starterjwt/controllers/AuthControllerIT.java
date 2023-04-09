package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<LoginRequest> loginRequestJacksonTester;

    @Autowired
    private JacksonTester<SignupRequest> signupRequestJacksonTester;

    @Autowired
    private UserRepository userService;

    private static LoginRequest loginRequest;
    private static SignupRequest signupRequest;

    @BeforeAll
    static void init() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        signupRequest = new SignupRequest();
        signupRequest.setFirstName("UserF");
        signupRequest.setLastName("UserL");
        signupRequest.setPassword("pass123");
        signupRequest.setEmail("user@user.com");
    }

    @Test
    @DisplayName("Test authenticateUser")
    void testAuthenticateUser() throws Exception {
        String json = loginRequestJacksonTester.write(loginRequest).getJson();

        MockHttpServletResponse result = mvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk()).andReturn().getResponse();

        String token = result.getContentAsString();
        Assertions.assertNotNull(token);
    }

    @Test
    @DisplayName("Test authenticateUser Bad credential")
    void testAuthenticateUserBedCredential() throws Exception {
        loginRequest.setPassword("00000");
        String json = loginRequestJacksonTester.write(loginRequest).getJson();

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isUnauthorized()).andReturn();

    }

    @Test
    @DisplayName("Test registerUser")
    void registerUser() throws Exception {
        String json = signupRequestJacksonTester.write(signupRequest).getJson();

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully!"))).andReturn();
    }

    @Test
    @DisplayName("Test registerUser user exist")
    void registerUserExist() throws Exception {
        signupRequest.setEmail("yoga@studio.com");
        String json = signupRequestJacksonTester.write(signupRequest).getJson();

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Error: Email is already taken!"))).andReturn();
    }

    @AfterAll
    void end() {
        String email = "user@user.com";
        if (userService.existsByEmail(email)) {
            User user = userService.findByEmail(email).get();
            userService.deleteById(user.getId());
        }
    }
}
