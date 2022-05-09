package com.example.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeForm {

    private String firstName;
    private String lastName;
    private int age;
    private String PESEL;
    private String cityRegistered;
    private String streetRegistered;
    private String houseNrRegistered;
    private String flatNrRegistered;
    private String cityResidence;
    private String streetResidence;
    private String houseNrResidence;
    private String flatNrResidence;
    private String departmentName;
    private String salary;
    private String position;
}
