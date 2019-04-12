package com.google.swarm.istio.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.swarm.istio.model.Registration;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistrationServiceClient {
	private final RestTemplate restTemplate;
	 public List<Registration> getRegistrations(String studentId)
	    {
			//http://students-service:8080
	    	
	    	 ResponseEntity<List<Registration>> response = restTemplate.exchange("http://registration-service:8080/registrations/{studentId}",
	                 HttpMethod.GET, HttpEntity.EMPTY,
	                 new ParameterizedTypeReference<List<Registration>>() {},
	                 studentId);
	    	List<Registration> registrations = response.getBody();
	    	return registrations;
	    	
	    }
}
