package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureJsonTesters
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<SessionDto> sessionJacksonTester;

    @Autowired
    private SessionRepository repository;

    private SessionDto session;

    @BeforeEach
    void start() {
        Long id = 1L;

        List<Long> userList = new ArrayList<>();
        userList.add(1L);
        session = new SessionDto(id, "Session", new Date(), 1L, "Description", userList,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Test sessionCreate and delete")
    @WithMockUser(username = "yoga@studio.com")
    void testSessionCreate() throws Exception {
        mvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJacksonTester.write(session).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Session"))).andReturn().getResponse();
    }

    @Test
    @DisplayName("Test sessionCreate Unauthorized")
    void testSessionCreateUnauthorized() throws Exception {
        mvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJacksonTester.write(session).getJson()))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @DisplayName("Test findAll")
    @WithMockUser(username = "yoga@studio.com")
    void testSessionFindAll() throws Exception {
        mvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(session.getName())))
                .andExpect(jsonPath("$[0].id", is(1))).andReturn().getResponse();
    }

    @Test
    @DisplayName("Test findAll Unauthorized")
    void testSessionFindAllUnauthorized() throws Exception {
        mvc.perform(get("/api/session"))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    @DisplayName("Test findById NotFound")
    @WithMockUser(username = "yoga@studio.com")
    void testSessionFindByIdNotFound() throws Exception {
        mvc.perform(get("/api/session/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test update Unauthorized")
    void testSessionUpdateUnauthorized() throws Exception {
        mvc.perform(post("/api/session/12")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionJacksonTester.write(session).getJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test delete Unauthorized")
    void testSessionDeleteUnauthorized() throws Exception {
        mvc.perform(delete("/api/session/12"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test delete NotFound")
    @WithMockUser(username = "yoga@studio.com")
    void testSessionDeleteNotFound() throws Exception {
        mvc.perform(delete("/api/session/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test noLongerParticipate no found")
    @WithMockUser(username = "yoga@studio.com")
    void testSessionNoLongerParticipateNotFound() throws Exception {
        mvc.perform(delete("/api/session/1000/participate/1000"))
                .andExpect(status().isNotFound());
    }

    @AfterAll
    void end() {
        repository.deleteAll();
    }
}
