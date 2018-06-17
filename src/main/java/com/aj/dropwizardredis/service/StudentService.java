package com.aj.dropwizardredis.service;

import com.aj.dropwizardredis.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService() {
    }

    public static Student getFromDatabase(String universityId) {
        Student student1 = new Student("Jim", "S100", "Science");
        Student student2 = new Student("Steve", "M101", "Maths");
        Student student3 = new Student("Mark", "P102", "Physics");
        Map<String, Student> database = new HashMap<>();
        database.put("S100", student1);
        database.put("M101", student2);
        database.put("P102", student3);
        logger.info("Database called for: {}", universityId);
        return database.get(universityId);
    }
}
