package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionServiceTest {

    private Session session;
    private User user;

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void start() {
        User user1 = new User(1L,"user@user.com", "User", "Usr", "pass",
                false, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(2L,"user2@user.com", "User2", "Usr2", "pass2",
                true, LocalDateTime.now(), LocalDateTime.now());
        Teacher teacher = new Teacher();
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        session = new Session(1L,"Session1",new Date(),"Description", teacher, userList,
                LocalDateTime.now(), LocalDateTime.now());
        user = new User();
        user.setId(5L);
    }

    @Test
    @DisplayName("Test crate Session")
    void testCreate() {
        doReturn(session).when(sessionRepository).save(any());

        Session returnedSession = sessionService.create(session);

        Assertions.assertNotNull(returnedSession, "The saved session should not be null");
        Assertions.assertEquals(session, returnedSession, "Sessions should de equals");
    }

    @Test
    @DisplayName("Test delete session when session Id is null")
    void testDeleteSessionIdNull() {
        doThrow(IllegalArgumentException.class).when(sessionRepository).deleteById(null);

        Throwable exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    sessionService.delete(null);
                });

        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test delete session executed one time")
    void testDeleteSession() {
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test find all session")
    void testFindAllSession() {
        List<Session> sessionList = Arrays.asList(session, session);
        doReturn(sessionList).when(sessionRepository).findAll();

        List<Session> response = sessionService.findAll();

        Assertions.assertNotNull(response, "Returned sessions should not be null");
        Assertions.assertEquals(sessionList.size(), response.size());
    }

    @Test
    @DisplayName("Test getById Success")
    void testGetById() {
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());

        Session returnedSession = sessionService.getById(1L);

        Assertions.assertNotNull(returnedSession);
    }

    @Test
    @DisplayName("Test getById Not Found")
    void testGetByIdNotFound() {
        doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());

        Session returnedSession = sessionService.getById(1L);

        Assertions.assertNull(returnedSession);
    }

    @Test
    @DisplayName("Test update")
    void testUpdate() {
        doReturn(session).when(sessionRepository).save(any());

        Session returnedSession = sessionService.update(1L, session);

        Assertions.assertNotNull(returnedSession);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("Test participate")
    void testParticipate() {
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());

        sessionService.participate(1L, 5L);

        verify(userRepository, times(1)).findById(anyLong());
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("Test participate user and session are null")
    void testParticipateUserAndSessionNull() {
        doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());
        doReturn(Optional.empty()).when(userRepository).findById(anyLong());

        Throwable exception = Assertions.assertThrows(
                NotFoundException.class, () -> {
                    sessionService.participate(1L, 1L);
                });

        Assertions.assertEquals(NotFoundException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test participate the user is already participate the session throws BedRequestException")
    void testParticipateAlreadyParticipate() {
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
        doReturn(Optional.of(user)).when(userRepository).findById(anyLong());

        Throwable exception = Assertions.assertThrows(
                BadRequestException.class, () -> {
                    sessionService.participate(1L, 1L);
                });

        Assertions.assertEquals(BadRequestException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test noLongerParticipation")
    void testNoLongerParticipation() {
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());

        sessionService.noLongerParticipate(1L, 1L);

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("Test noLongerParticipate throw NoFoundException")
    void testNoLongerParticipateNotFound() {
        doReturn(Optional.empty()).when(sessionRepository).findById(anyLong());
        Throwable exception = Assertions.assertThrows(
                NotFoundException.class, () -> {
                    sessionService.noLongerParticipate(1L, 1L);
                });

        Assertions.assertEquals(NotFoundException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test noLongerParticipate throw BadRequestException")
    void testNoLongerParticipateBadRequest() {
        doReturn(Optional.of(session)).when(sessionRepository).findById(anyLong());
        Throwable exception = Assertions.assertThrows(
                BadRequestException.class, () -> {
                    sessionService.noLongerParticipate(1L, 5L);
                });

        Assertions.assertEquals(BadRequestException.class, exception.getClass());
    }
}
