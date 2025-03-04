package com.example.demo.StudentService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.StudentRepository.StudentRepository;
import com.example.demo.bean.StudentRecordDetails;


@Service
public class StudentService {
	@Autowired
	StudentRepository repository;

	private static final String UPLOAD_DIR = "studentphotos/"; // folder chi link

	public StudentRecordDetails registerNewStudent(StudentRecordDetails student, MultipartFile file) {
		try {

			long startTime=System.currentTimeMillis(); //testing for time start time
			
			
			File directory = new File(UPLOAD_DIR); // file cha object folder represent karnya sathi
			if (!directory.exists()) {     // directory nasel tr
				directory.mkdirs();			// new directoty banvel
			}

			
			
			String fileName =   file.getOriginalFilename(); //upload myphoto.jpg,then fileName will be "myphoto.jpg"
			Path filePath = Paths.get(UPLOAD_DIR, fileName);// combine foldername + file name
			Files.write(filePath, file.getBytes());
			
			
			long endTime=System.currentTimeMillis();// end time


			System.out.println("end Time: " + endTime + " ms");
			
	        System.out.printf("registerNewStudent execution time: " + (endTime-startTime) + " ms");

	        //18,22,14,28,21,29,18
	        
	        // 4mb - 75ms,47ms,52ms,65ms,30ms,48ms,36
	        
			student.setPhotoPath(filePath.toString());

			return repository.save(student);
		} catch (IOException e) {
			throw new RuntimeException("Error saving file", e);
		}
	}

	public Resource getStudentPhoto(int id) {
		
		
		long startTime = System.currentTimeMillis();
		
		
		 Optional<StudentRecordDetails> student = repository.findById(id);
		 
		 if(student.isPresent()) {
			 String filePath = student.get().getPhotoPath();
			 try {
				 Path path = Paths.get(filePath);
				 Resource resource = new UrlResource(path.toUri());
				 if(resource.exists() || resource.isReadable()) {
					 
					 long endTime = System.currentTimeMillis();
					 

					 System.out.println("end Time: " + endTime + " ms");
						
		             System.out.println("getStudentPhoto execution time: " + (endTime - startTime) + " ms");
		             
		             // 4ms,3ms,4ms,4ms 
		             // 4mb - 123ms,4ms,4ms,7ms,5ms
		             
		             
					 return resource;
					 
				 }else {
					 throw new RuntimeException("File not Found or not readable : "+ filePath);
					 
				 }
				 
			 }catch(Exception e) {
				 throw new RuntimeException("Error retrieving file",e);
			 }
		 }
		return null;
	}

	public String removeStudentById(int id) {
		
		long startTime = System.currentTimeMillis();
		
		
		if(repository.existsById(id)) {
			repository.deleteById(id);
			
			long endTime=System.currentTimeMillis();// end time
			

			System.out.println("end Time: " + endTime + " ms");
				
            System.out.println("removeStudentById execution time: " + (endTime - startTime) + " ms");
             
            
            // 27,19,15 milliseconds
            // (595 1st time executio
            // 4bm - 18ms,12ms
			return "Student Removed Successfully";
			
		}else {
			return "Student with id "+ id +"Not Prsent in Database";
		}
	}
	
	public String updateStudent(int id, String name, MultipartFile file) {
	    long startTime = System.currentTimeMillis();

	    Optional<StudentRecordDetails> optionalStudent = repository.findById(id);
	    if (optionalStudent.isPresent()) {
	        StudentRecordDetails student = optionalStudent.get();
	        student.setStudent_name(name);

	        try {
	            // Ensure upload directory exists
	            File directory = new File(UPLOAD_DIR);
	            if (!directory.exists()) {
	                boolean dirCreated = directory.mkdirs();
	                if (!dirCreated) {
	                    throw new RuntimeException("Failed to create directory: " + UPLOAD_DIR);
	                }
	            }

	            // Check if the old file path is valid
	            if (student.getPhotoPath() != null && !student.getPhotoPath().isEmpty()) {
	                Path oldFilePath = Paths.get(UPLOAD_DIR, student.getPhotoPath()); // Convert relative path to absolute
	                System.out.println("Stored file path in DB (relative): " + student.getPhotoPath());

	                // Only delete if the file actually exists
	                if (Files.exists(oldFilePath)) {
	                    System.out.println("File exists. Proceeding to delete: " + oldFilePath);
	                    Files.delete(oldFilePath);
	                } else {
                    System.out.println("File not found, skipping deletion: " + oldFilePath);
	                }
	            }

	            // Store only the relative path
	            String relativePath = "studentphotos/" + file.getOriginalFilename();  
	            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());

	            System.out.println("Saving updated file to: " + filePath);
	            file.transferTo(filePath.toFile());

	            // Update student photo path with RELATIVE path
	            student.setPhotoPath(relativePath);
	            repository.save(student);

	            long endTime = System.currentTimeMillis();
	            System.out.println("updateStudent execution time: " + (endTime - startTime) + " ms");

	            return "Student updated successfully!";
	        } catch (IOException e) {
	            throw new RuntimeException("Error while updating student details", e);
	        }
	    } else {
	        return "Student with ID " + id + " not found.";
	    }
	}

	

	
}
