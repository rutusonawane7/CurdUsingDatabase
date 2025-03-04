package com.example.demo.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Bean.Studentdetails;
import com.example.demo.Repository.StudentManagementRepository;

@Service
public class StudentManagementService {

	@Autowired
	StudentManagementRepository repository;
	
	
	public Studentdetails addNewStudent(Studentdetails student) {
		return repository.save(student);
	
	}


	public Studentdetails getUserById(int id) {
		
		return repository.findById(id).orElse(null);
	}


	public String DeleteStudentById(int id) {
		
		return null;
	}


	public String updateStudent(int id, String name, MultipartFile file) {
		long startTime = System.currentTimeMillis();

		
		Optional<Studentdetails> optionalStudent = repository.findById(id);
		
        if (optionalStudent.isPresent()) {
            Studentdetails student = optionalStudent.get();
            student.setStudnet_name(name); 
            
            try {
            	student.setStudent_photo(file.getInputStream().readAllBytes());       
            	} catch (IOException e) {
                throw new RuntimeException("Error while saving student photo", e);
            }

            repository.save(student);
            
            long endTime = System.currentTimeMillis(); 
    	    System.out.println("Execution time: " + (endTime - startTime) + " ms");
    		
            return "Student updated successfully!";
        } else {
            return "Student with ID " + id + " not found";
        }
	}
	

}
