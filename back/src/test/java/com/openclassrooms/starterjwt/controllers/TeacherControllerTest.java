package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
class TeacherControllerTest {

    @MockBean
    private TeacherService teacherService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherController teacherController;

    private Teacher teacher;
    private Teacher teacher2;

    @BeforeEach
    void start() {
        teacher = new Teacher(1L, "Teacher", "Teacher", LocalDateTime.now(), LocalDateTime.now());
        teacher2 = new Teacher(2L, "Teacher", "Teacher", LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    @DisplayName("Test findById")
    void testFindById() {
        doReturn(teacher).when(teacherService).findById(anyLong());

        ResponseEntity<?> response = teacherController.findById("1");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test findById teacher with selected id didn't find")
    void testFindByIdNotFound() {
        doReturn(null).when(teacherService).findById(anyLong());

        ResponseEntity<?> response = teacherController.findById("1");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Test findById , id isn't number")
    void testFindByIdIsNotNumber() {
        doThrow(NumberFormatException.class).when(teacherService).findById(anyLong());
        ResponseEntity<?> response = null;

        try {
            response = teacherController.findById("notNumber");
        } catch (NumberFormatException e) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        teachers.add(teacher2);
        doReturn(teachers).when(teacherService).findAll();

        ResponseEntity<?> response = teacherController.findAll();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
