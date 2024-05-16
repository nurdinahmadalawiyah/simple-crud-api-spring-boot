package com.spring.crud.service;

import com.spring.crud.model.Student;
import com.spring.crud.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    public StudentServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setName("John Doe");
        student.setEmail("john.doe@example.com");

        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);

        assertEquals("John Doe", createdStudent.getName());
        assertEquals("john.doe@example.com", createdStudent.getEmail());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    public void testGetStudentById() {
        Student student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("john.doe@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> retrievedStudent = studentService.getStudentById(1L);

        assertTrue(retrievedStudent.isPresent());
        assertEquals("John Doe", retrievedStudent.get().getName());
        assertEquals("john.doe@example.com", retrievedStudent.get().getEmail());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateStudent() {
        Student existingStudent = new Student();
        existingStudent.setId(1L);
        existingStudent.setName("John Doe");
        existingStudent.setEmail("john.doe@example.com");

        Student updatedDetails = new Student();
        updatedDetails.setName("Jane Doe");
        updatedDetails.setEmail("jane.doe@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedDetails);

        Student updatedStudent = studentService.updateStudent(1L, updatedDetails);

        assertEquals("Jane Doe", updatedStudent.getName());
        assertEquals("jane.doe@example.com", updatedStudent.getEmail());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(existingStudent);
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setName("John Doe");
        student.setEmail("john.doe@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).deleteById(1L);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
