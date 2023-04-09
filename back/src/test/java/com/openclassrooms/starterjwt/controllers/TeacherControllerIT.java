package com.openclassrooms.starterjwt.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Test teacherFindAll")
    @WithMockUser(username = "yoga@studio.com")
    void testTeacherFindAll() throws Exception {
        mvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName", is("DELAHAYE"))).andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @DisplayName("Test teacherFindAll Unauthorized")
    void testTeacherFindAllUnauthorized() throws Exception {
        mvc.perform(get("/api/teacher"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", Matchers.is("Full authentication is required to access this resource"))).andReturn();
    }

    @Test
    @DisplayName("Test teacherFindById")
    @WithMockUser(username = "yoga@studio.com")
    void testTeacherFindById() throws Exception {
        mvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", Matchers.is("DELAHAYE")))
                .andExpect(jsonPath("$.id", Matchers.is(1))).andReturn();
    }

    @Test
    @DisplayName("Test teacherFindById ID not found")
    @WithMockUser(username = "yoga@studio.com")
    void testTeacherFindByIdNotFound() throws Exception {
        mvc.perform(get("/api/teacher/1000"))
                .andExpect(status().isNotFound()).andReturn();
    }

}
