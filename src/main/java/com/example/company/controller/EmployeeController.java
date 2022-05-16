package com.example.company.controller;

import com.example.company.model.EmployeeForm;
import com.example.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<?> createEmployee(EmployeeForm employeeForm) {
        return employeeService.createNewEmployee(employeeForm);
    }

    @DeleteMapping("/employee/{id}")
    @ResponseBody
    ResponseEntity deleteEmployee(@PathVariable Long id) {
        return employeeService.deleteEmployeeById(id);
    }

    @PutMapping("/employee/{id}")
    @ResponseBody
    ResponseEntity<?> editEmployee(@PathVariable Long id, EmployeeForm employeeForm) {
        return employeeService.updateEmployee(id, employeeForm);
    }
}
