package com.example.company.repository;

import com.example.company.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO employee (id) VALUES (:id)", nativeQuery = true)
    void insertNewEmployee(@Param("id") Long id);
}
