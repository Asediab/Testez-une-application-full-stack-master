package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Test
    @DisplayName("Test delete user when user Id is null")
    void testDeleteUserIdNull() {
        doThrow(IllegalArgumentException.class).when(userRepository).deleteById(null);

        Throwable exception = Assertions.assertThrows(
                IllegalArgumentException.class, () -> {
                    userService.delete(null);
                });

        Assertions.assertEquals(IllegalArgumentException.class, exception.getClass());
    }

    @Test
    @DisplayName("Test delete user executed one time")
    void testDeleteUser() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        doReturn(Optional.of(new User())).when(userRepository).findById(anyLong());

        User returnedUser = userService.findById(1L);

        Assertions.assertNotNull(returnedUser);
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        doReturn(Optional.empty()).when(userRepository).findById(anyLong());

        User returnedUser = userService.findById(1L);

        Assertions.assertNull(returnedUser);
    }
}
