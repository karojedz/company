package com.example.company.controller;

import com.example.company.model.Employee;
import com.example.company.model.EmployeeForm;
import com.example.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employee")
    String addEmployee() {
        return "add";
    }

    @PostMapping("/employee")
    @ResponseBody
    ResponseEntity<Employee> createEmployee(EmployeeForm employeeForm) {
        return ResponseEntity.ok(employeeService.createNewEmployee(employeeForm));
    }

    @DeleteMapping("/employee/{id}")
    @ResponseBody
    ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.deleteEmployeeById(id));
    }

    @PutMapping("/employee/{id}")
    @ResponseBody
    ResponseEntity<String> editEmployee(@PathVariable Long id, EmployeeForm employeeForm) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeForm));
    }
}
