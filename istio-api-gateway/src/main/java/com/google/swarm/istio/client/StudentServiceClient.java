package com.google.swarm.istio.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.swarm.istio.model.Student;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StudentServiceClient {
   
	private final RestTemplate restTemplate;

    public List<Student> getStudents()
    {
		//http://students-service:8080
    	ResponseEntity<List<Student>> response =
    	        restTemplate.exchange("http://student-service:8080/students",
    	                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
    	            });
    	List<Student> students = response.getBody();
    	return students;
    	
    }

    public Student getStudent(final String studentId) {
		
        Student student= restTemplate.getForObject("http://student-service:8080/students/{studentId}", Student.class, studentId);

    	return student;
    	
    }

}
