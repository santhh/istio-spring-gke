package com.google.swarm.istio.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity

@Table(name = "students_data")
public class Student {
	@Id
	@Column(name = "id")
	private String studentId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	public Student() {
	}

	public Student(String id, String firstName, String lastName) {
		this.studentId = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Student{" +
				"studentId='" + this.studentId + '\'' +
				", firstName='" + this.firstName + '\'' +
				", lastName='" + this.lastName + '\'' +
				'}';
	}
	
}
