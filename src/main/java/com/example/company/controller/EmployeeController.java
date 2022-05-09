package com.example.company.controller;

import com.example.company.model.EmployeeForm;
import com.example.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/add")
    String addEmployee() {
        return "add";
    }

    @PostMapping("/add")
    @ResponseBody
    String createEmployee(EmployeeForm employeeForm) {
        employeeService.createNewEmployee(employeeForm);
        return "Data submitted successfully";
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return "Employee deleted";
    }

    @PutMapping("/edit/{id}")
    @ResponseBody
    String editEmployee(@PathVariable Long id, EmployeeForm employeeForm) {
        employeeService.updateEmployee(id, employeeForm);
        return "Update received";
    }
}
