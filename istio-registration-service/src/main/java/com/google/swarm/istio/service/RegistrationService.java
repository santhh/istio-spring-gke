package com.google.swarm.istio.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.data.spanner.core.SpannerOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spanner.Key;
import com.google.swarm.istio.model.Registration;
import com.google.swarm.istio.repository.RegistrationRepository;

@RestController
@RequestMapping("/registrations")
public class RegistrationService {
    @Autowired
    private SpannerOperations spannerOperations;
    @Autowired
    private RegistrationRepository repository;
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<Registration>> getRegistrations() {
        Collection<Registration> registrations = this.spannerOperations.readAll(Registration.class);
        if (registrations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }



    @SuppressWarnings("unused")
	@RequestMapping(value = "/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<Registration>> getRegistrationByStudentId(@PathVariable("studentId") String studentId) {
       Collection<Registration> registrations = null;
        
        registrations = this.repository.findByStudentId(studentId);
        
        registrations.forEach(registration->{
        	registration.setCourseId("CSC101-Introduction to Computer Science");
        });

        if (registrations == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(registrations, HttpStatus.OK);
    }


}
