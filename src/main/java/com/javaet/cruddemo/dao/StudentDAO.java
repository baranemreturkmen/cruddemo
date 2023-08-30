package com.javaet.cruddemo.dao;

import com.javaet.cruddemo.entity.Student;

import java.util.List;

public interface StudentDAO {

    void save(Student student);

    Student findById(Integer id);

    List<Student> findAll();

    List<Student> findByLastName (String theLastName);

    void update(Student theStudent);

    int updateLastNameWithLastName(String lastName,String newLastName);

    void delete(Integer id);

    int deleteStudentWithName(String firstName);

    int deleteAll();

}
