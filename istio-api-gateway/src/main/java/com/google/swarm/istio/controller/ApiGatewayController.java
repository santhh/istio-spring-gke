package com.google.swarm.istio.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.swarm.istio.client.RegistrationServiceClient;
import com.google.swarm.istio.client.StudentServiceClient;
import com.google.swarm.istio.model.Registration;
import com.google.swarm.istio.model.Student;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ApiGatewayController {
	static private final Logger logger = LoggerFactory.getLogger(ApiGatewayController.class);
    private final StudentServiceClient studentServiceClient;
    private final RegistrationServiceClient registrationServiceClient;
    
    @GetMapping(value = "/students/{studentId}")
    public Student getStudent(final @PathVariable String studentId) {
    	
    	logger.info("Getting Students by Student Id: {}", studentId);
    	final Student student = studentServiceClient.getStudent(studentId);
    	logger.info("Getting Registrations for Student Id: {}", student.getStudentId());
    	List <Registration> registrations = registrationServiceClient.getRegistrations(student.getStudentId());
    	logger.info("Registration Count: "+registrations.size());
    	student.setRegistrations(registrations);
        return student;
    }
    
    @GetMapping(value = "/students")
    public List<Student> getStudents() {
    	
    	logger.info("Getting ALL Students");
    	final List<Student> students = studentServiceClient.getStudents();
        return students;
    }
}
