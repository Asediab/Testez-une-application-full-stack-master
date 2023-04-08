package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class SessionControllerTest {

    @Autowired
    private SessionMapper sessionMapper;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private SessionController sessionController;

    private final Long id = 1L;
    private final String email = "yoga@studio.com";
    private final String firstName = "Admin";
    private final String lastName = "Admin";
    private final String password = "test!1234";
    private Session session;

    @BeforeEach
    void start() {
        User user1 = new User(id,email, lastName, firstName, password, false, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(id,email, lastName, firstName, password, false, LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher = new Teacher(id,lastName, firstName,LocalDateTime.now(),LocalDateTime.now());
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        session = new Session(id, "Session", new Date(), "description", teacher, userList,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Test findById")
    void testFindById() {
        doReturn(session).when(sessionService).getById(anyLong());

        ResponseEntity<?> response = sessionController.findById("1");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test findById session Id is Null")
    void testFindByIdSessionIdNull() {
        doReturn(null).when(sessionService).getById(anyLong());

        ResponseEntity<?> response = sessionController.findById("1");

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test findById NumberFormatException")
    void testFindByIdNumberFormatException() {
        doThrow(NumberFormatException.class).when(sessionService).getById(anyLong());
        ResponseEntity<?> response = null;
        try {
              response = sessionController.findById("notNumber");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test finAll")
    void testFindAll() {
        List<Session> sessionList = new ArrayList<>();
        sessionList.add(session);
        sessionList.add(session);
        List<SessionDto> sessionDtoList = new ArrayList<>();
        sessionDtoList.add(sessionMapper.toDto(session));
        sessionDtoList.add(sessionMapper.toDto(session));
        doReturn(sessionList).when(sessionService).findAll();

        ResponseEntity<?> response = sessionController.findAll();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(response.getBody(), sessionDtoList);
    }

    @Test
    @DisplayName("Test update NumberFormatException")
    void testUpdateNumberFormatException() {
        doThrow(NumberFormatException.class).when(sessionService).update(anyLong(), any(Session.class));
        ResponseEntity<?> response = null;
        try {
            response = sessionController.update("notNumber", sessionMapper.toDto(session));
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test save delete mapping")
    void testSave() {
        doReturn(session).when(sessionService).getById(anyLong());

        ResponseEntity<?> response = sessionController.save("1");

        verify(sessionService, times(1)).delete(anyLong());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test save delete mapping, Id is null")
    void testSaveIdIsNull() {
        doReturn(null).when(sessionService).getById(anyLong());

        ResponseEntity<?> response = sessionController.save("1");

        verify(sessionService, times(0)).delete(anyLong());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test save delete mapping, Id isn't a number")
    void testSaveIdIsNotNumber() {
        doThrow(NumberFormatException.class).when(sessionService).getById(null);

        ResponseEntity<?> response = sessionController.save("notNumber");

        verify(sessionService, times(0)).delete(anyLong());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Test participate")
    void testParticipate() {
        doNothing().when(sessionService).participate(anyLong(), anyLong());

        ResponseEntity<?> response = sessionController.participate("1", "2");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test participate, Ids isn't a number")
    void testParticipateIdIsNotNumber() {
        doThrow(NumberFormatException.class).when(sessionService).participate(anyLong(), anyLong());
        ResponseEntity<?> response = null;
        try {
            response = sessionController.participate("notNumber", "notNumber");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test noLongerParticipate")
    void testNoLongerParticipate() {
        doNothing().when(sessionService).noLongerParticipate(anyLong(), anyLong());

        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "2");

        verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test noLongerParticipate, Ids isn't a number")
    void testNoLongerParticipateIdIsNotNumber() {
        doThrow(NumberFormatException.class).when(sessionService).noLongerParticipate(anyLong(), anyLong());

        ResponseEntity<?> response = sessionController.noLongerParticipate("notNumber", "notNumber");

        verify(sessionService, times(0)).noLongerParticipate(anyLong(), anyLong());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
