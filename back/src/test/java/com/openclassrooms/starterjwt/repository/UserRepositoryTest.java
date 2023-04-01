package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class UserRepositoryTest {

    @MockBean
    private UserRepository userRepository;

    private static final User user = new User("user@user.com", "User", "user",
            "pass", true);

    @Test
    @DisplayName("Test findByEmail")
    void testFindByEmail() {
        doReturn(Optional.of(user)).when(userRepository).findByEmail(anyString());

        Optional<User> userOptional = userRepository.findByEmail("user@user.com");

        Assertions.assertTrue(userOptional.isPresent());
        Assertions.assertEquals(userOptional.get().getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("Test existByEmail")
    void testExistByEmail() {
        doReturn(true).when(userRepository).existsByEmail(anyString());

        boolean userExist = userRepository.existsByEmail("user@user.com");

        Assertions.assertTrue(userExist);
    }

    @Test
    @DisplayName("Test existByEmail user isn't exist")
    void testExistByEmailUserNotExist() {
        doReturn(false).when(userRepository).existsByEmail(anyString());

        boolean userNotExist = userRepository.existsByEmail("user@user.com");

        Assertions.assertFalse(userNotExist);
    }
}