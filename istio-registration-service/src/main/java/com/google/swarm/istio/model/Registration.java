package com.google.swarm.istio.model;

import org.springframework.cloud.gcp.data.spanner.core.mapping.Column;
import org.springframework.cloud.gcp.data.spanner.core.mapping.PrimaryKey;
import org.springframework.cloud.gcp.data.spanner.core.mapping.Table;

@Table(name = "registrations")
public class Registration {
	@PrimaryKey
	@Column(name = "registration_id")
	private String registrationId;

	@Column(name = "student_id")
	private String studentId;

	@Column(name = "course_id")
	private String courseId;
	public Registration() {
	}

	public Registration(String registrationId, String studentId, String courseId) {
		this.registrationId= registrationId;
		this.studentId= studentId;
		this.courseId = courseId;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "Registration [registrationId=" + registrationId + ", studentId=" + studentId + ", courseId=" + courseId
				+ "]";
	}

}
