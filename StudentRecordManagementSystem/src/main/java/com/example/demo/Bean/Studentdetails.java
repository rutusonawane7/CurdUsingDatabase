package com.example.demo.Bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Studentdetails {
	
	@Id
	@GeneratedValue
	private int student_id;
	
	private String studnet_name;
	
	private byte[] student_photo;

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getStudnet_name() {
		return studnet_name;
	}

	public void setStudnet_name(String studnet_name) {
		this.studnet_name = studnet_name;
	}

	public byte[] getStudent_photo() {
		return student_photo;
	}

	public void setStudent_photo(byte[] student_photo) {
		this.student_photo = student_photo;
	}
	
	
	

}
