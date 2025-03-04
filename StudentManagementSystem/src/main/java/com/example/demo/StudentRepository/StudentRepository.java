package com.example.demo.StudentRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.bean.StudentRecordDetails;

@Repository
public interface StudentRepository extends JpaRepository<StudentRecordDetails, Integer>{

}
