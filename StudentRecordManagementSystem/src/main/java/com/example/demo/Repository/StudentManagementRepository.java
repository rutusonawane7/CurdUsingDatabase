package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Bean.Studentdetails;

public interface StudentManagementRepository extends JpaRepository<Studentdetails, Integer>{

}
