package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Bean.Studentdetails;
import com.example.demo.Service.StudentManagementService;

@RestController
public class StudentManagementController {
	
	@Autowired
	StudentManagementService service;
	
	
	@PostMapping("add")
	public ResponseEntity<Studentdetails> addNewStudent(
			@RequestPart ("name") String name, 
			@RequestPart ("photo") MultipartFile file){
		
		
		long startTime = System.currentTimeMillis();
		
		Studentdetails student = new Studentdetails();
		student.setStudnet_name(name);
		
		try {
				byte[] photoBytes= file.getBytes();
				student.setStudent_photo(photoBytes);
		
		}catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			
		}
		
		Studentdetails savedStudent = service.addNewStudent(student);
		
		long endTime = System.currentTimeMillis(); 
	    System.out.println("Execution time: " + (endTime - startTime) + " ms");
	    
	    //667 ms,34,ms,30
	    // 1st time execution 545 ms
	    //4 mb - 297ms,262 ms, 319ms,305 ms,382 ms

		return ResponseEntity.status(HttpStatus.CREATED).body(savedStudent);
		
	}

	@GetMapping("student/{id}")
	public ResponseEntity<byte[]> getUserById(@PathVariable int id){
		
		long startTime = System.currentTimeMillis();
		
		
		Studentdetails student = service.getUserById(id);
		
		byte[] file = student.getStudent_photo();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		
		
		long endTime = System.currentTimeMillis(); 
	    System.out.println("Execution time: " + (endTime - startTime) + " ms");
	    
	    //12ms
	    // 1st time excecution 105ms
	    // 4mb - 123ms, 48ms,34 ms,79ms
		
		return new ResponseEntity<>(file,headers,HttpStatus.OK);
		
	}
	
	/*@DeleteMapping("/student/{id}")
	public ResponseEntity<String> DeleteStudentById(@PathVariable int id){
		
		long startTime = System.currentTimeMillis();

		
		String deteledStudent = service.DeleteStudentById(id);
		
		if(deteledStudent.equals("Student Deleted Successfully from Database")) {
			
			long endTime = System.currentTimeMillis(); 
		    System.out.println("Execution time: " + (endTime - startTime) + " ms");
			
			return new ResponseEntity<>(deteledStudent,HttpStatus.OK);
		}
		else
		{
			long endTime = System.currentTimeMillis(); 
		    System.out.println("Execution time: " + (endTime - startTime) + " ms");
			
			return new ResponseEntity<>(deteledStudent,HttpStatus.NOT_FOUND);
		}*/
	
	@DeleteMapping("/student/{id}")
	public ResponseEntity<String> DeleteStudentById(@PathVariable int id) {
	    
	    long startTime = System.currentTimeMillis();

	    String deletedStudent = service.DeleteStudentById(id);

	    long endTime = System.currentTimeMillis(); 
	    System.out.println("Execution time: " + (endTime - startTime) + " ms");

	    if ("Student Deleted Successfully from Database".equals(deletedStudent)) {
	        return new ResponseEntity<>(deletedStudent, HttpStatus.OK);
	    } else if (deletedStudent == null) {
	        return new ResponseEntity<>("Error: Student not found in Database", HttpStatus.NOT_FOUND);
	    } else {
	        return new ResponseEntity<>(deletedStudent, HttpStatus.NOT_FOUND);
	    }
	}


	
	@PutMapping("/update/student/{id}")
	public ResponseEntity<String> updateStudent(
			@PathVariable int id, 
			@RequestParam ("student_name") String name,
			@RequestPart("student_photo") MultipartFile file){
		
		String response = service.updateStudent(id,name,file);
		
		return ResponseEntity.ok(response);
		
	}

}
