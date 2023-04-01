package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BadRequestExceptionTest {
    @Test
    @DisplayName("Should throws BadRequestException with HttpStatus.BAD_REQUEST")
    void shouldBadRequestException() {
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException();
        });
        assertEquals(HttpStatus.BAD_REQUEST, exception.getClass().getAnnotation(ResponseStatus.class).value());
    }
}
