package com.google.swarm.istio.repository;

import java.util.List;

import org.springframework.cloud.gcp.data.spanner.repository.SpannerRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.google.swarm.istio.model.Registration;


@RepositoryRestResource(collectionResourceRel = "repo", path = "repo")
public interface RegistrationRepository extends SpannerRepository<Registration, String>  {
	List<Registration> findByStudentId(String studentId);
}