package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotFoundExceptionTest {
    @Test
    @DisplayName("Should throws NotFoundException with HttpStatus.NOT_FOUND")
    void shouldNotFoundExceptionTest() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException();
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getClass().getAnnotation(ResponseStatus.class).value());
    }
}
