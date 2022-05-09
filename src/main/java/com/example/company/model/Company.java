package com.example.company.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Company {

    private String name = "Company LTD";
    private Address address;
    private List<Employee> employees = new ArrayList<>();
}
