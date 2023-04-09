package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIT {

    @Autowired
    private MockMvc mvc;

    private String userJson;
    private SignupRequest signupRequest;
    private LoginRequest loginRequest;

    @Autowired
    private JacksonTester<SignupRequest> signupRequestJacksonTester;

    @Autowired
    private JacksonTester<LoginRequest> loginRequestJacksonTester;

    @Autowired
    private JacksonTester<User> userJacksonTester;

    @BeforeAll
    void start() throws IOException {
        User user = new User(1L,"user@user.com", "User", "Usr", "pass",
                false, LocalDateTime.now(), LocalDateTime.now());
        userJson = userJacksonTester.write(user).getJson();

        signupRequest = new SignupRequest();
        signupRequest.setFirstName("UserF");
        signupRequest.setLastName("UserL");
        signupRequest.setPassword("pass123");
        signupRequest.setEmail("user@user.com");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("user@user.com");
        loginRequest.setPassword("pass123");

    }

    @Test
    @DisplayName("Test userFindById")
    @WithMockUser(username = "yoga@studio.com")
    void testUserFindById() throws Exception {
        mvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("yoga@studio.com")))
                .andExpect(jsonPath("$.id", is(1))).andReturn();
    }

    @Test
    @DisplayName("Test userFindById Unauthorized")
    void testUserFindByIdUnauthorized() throws Exception {
        mvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", Matchers.is("Full authentication is required to access this resource"))).andReturn();
    }

    @Test
    @DisplayName("Test userFindById ID not found")
    @WithMockUser(username = "yoga@studio.com")
    void testUserFindByIdNotFound() throws Exception {
        mvc.perform(get("/api/user/1000"))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @DisplayName("Test userSave")
    @WithMockUser(username = "yoga@studio.com")
    void testUserSave() throws Exception {
        mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJacksonTester.write(signupRequest).getJson()))
                .andExpect(status().isOk()).andReturn();

        MockHttpServletResponse response = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJacksonTester.write(loginRequest).getJson()))
                .andExpect(status().isOk()).andReturn().getResponse();
        JSONObject jObject = new JSONObject(response.getContentAsString());
        int id = jObject.getInt("id");
        mvc.perform(delete("/api/user/" + id)).andExpect(status().isOk());
        mvc.perform(get("/api/user/" + id)).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test UserSave ID not found")
    @WithMockUser(username = "yoga@studio.com")
    void testUserSaveIdNotFound() throws Exception {
        mvc.perform(delete("/api/user/1000"))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    @DisplayName("Test UserSave Unauthorized")
    void testUserSaveUnauthorized() throws Exception {
        mvc.perform(delete("/api/user"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", Matchers.is("Full authentication is required to access this resource"))).andReturn();
    }
}
