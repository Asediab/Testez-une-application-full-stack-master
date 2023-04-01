package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
class TeacherServiceTest {

    @Autowired
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @Test
    @DisplayName("Test find all teachers")
    void testFindAllTeacher() {
        List<Teacher> teacherList = Arrays.asList(new Teacher(), new Teacher());
        doReturn(teacherList).when(teacherRepository).findAll();

        List<Teacher> response = teacherService.findAll();

        Assertions.assertNotNull(response, "Returned Teacher should not be null");
        Assertions.assertEquals(teacherList.size(), response.size());
    }

    @Test
    @DisplayName("Test findById Success")
    void testFindById() {
        doReturn(Optional.of(new Teacher())).when(teacherRepository).findById(anyLong());

        Teacher returnedTeacher = teacherService.findById(1L);

        Assertions.assertNotNull(returnedTeacher);
    }

    @Test
    @DisplayName("Test findById Not Found")
    void testFindByIdNotFound() {
        doReturn(Optional.empty()).when(teacherRepository).findById(anyLong());

        Teacher returnedTeacher = teacherService.findById(1L);

        Assertions.assertNull(returnedTeacher);
    }
}
