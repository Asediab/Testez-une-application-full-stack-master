package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
class JwtUtilsTest {
    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    private final String email = "yoga@yoga.com";
    private final String firstName = "user";
    private final String lastName = "user";
    private final String password = "test!1234";
    private User user;
    private UserDetailsImpl userDetailsImpl;
    private static final String JWT_SECRET = "openclassrooms";
    private static final int JWT_EXPIRATION_MS = 86400000;

    @BeforeEach
    void start() {
        user = new User(1L, email, lastName, firstName, password, true, LocalDateTime.now(), LocalDateTime.now());
        userDetailsImpl = new UserDetailsImpl(1L, email, firstName, lastName, true, password);
    }

    @Test
    @DisplayName("Test getUserNameFromJwtToken")
    void testGetUserNameFromJwtToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);
        String name = jwtUtils.getUserNameFromJwtToken(token);

        Assertions.assertEquals(userDetailsImpl.getUsername(), name);
    }

    @Test
    @DisplayName("Test validateJwtToken")
    void testValidateJwtToken() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);

        String token = Jwts.builder().setSubject(userDetailsImpl.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET).compact();

        Assertions.assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Test validateJwtToken Token is mal Formed")
    void testValidateJwtTokenIsMalformed() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        Assertions.assertFalse(jwtUtils.validateJwtToken("malFormedToken"));
    }

    @Test
    @DisplayName("Test validateJwtToken Token is expired")
    void testValidateJwtTokenExpired() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);

        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);

        Assertions.assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    @DisplayName("Test validateJwtToken Token is empty")
    void testValidateJwtTokenIsEmpty() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);

        doReturn(userDetailsImpl).when(authentication).getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);

        Assertions.assertFalse(jwtUtils.validateJwtToken(""));
    }

    @Test
    @DisplayName("Test validateJwtToken signature not valid")
    void testValidateJwtTokenNotValidSignature() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);

        String token = Jwts.builder().setSubject(userDetailsImpl.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS512, "notValidSecret").compact();

        Assertions.assertFalse(jwtUtils.validateJwtToken(token));
    }

}
