package com.google.swarm.istio.jpa.service;


import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.swarm.istio.jpa.model.Student;
import com.google.swarm.istio.repositories.StudentRepository;

@RestController
@RequestMapping("/students")
@Service
public class StudentService
{

	
		@Autowired
		private StudentRepository studentRepo;
		
		

    /**
     * Get list of all students
     *
     * todo: add limit/offset support
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Collection<Student>> getStudents() {
    	Collection<Student> students=(Collection<Student>) this.studentRepo.findAll();
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }


    /**
     * Get single Student By id
     * @param studentId
     * @return
     */
    @RequestMapping(value = "/{studentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Optional<Student> > getStudent(@PathVariable("studentId") long studentId) {
        Optional<Student> student = null;
        student = this.studentRepo.findById(studentId);

        if (student == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }


    /**
     * Create new Student
     * @param student
     * @param ucBuilder
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Student> addStudent(@RequestBody @Valid Student student, UriComponentsBuilder ucBuilder) {

        this.studentRepo.save(student);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/students/{id}").buildAndExpand(student.getStudentId()).toUri());
        return new ResponseEntity<>(student, headers, HttpStatus.CREATED);
    }


  
   

}

