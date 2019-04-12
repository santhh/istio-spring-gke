package com.google.swarm.istio.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.swarm.istio.jpa.model.Student;


@Repository
public interface StudentRepository extends JpaRepository<Student, Long>  {

}
