package com.example.company.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Person person;

    @ManyToOne
    private Department department;

    private BigDecimal salary;
    private String position;

    public void setPerson(Person person) {
        this.person = person;
        person.setEmployee(this);
    }

    public void setDepartment(Department department) {
        this.department = department;
        department.getEmployees().add(this);
    }
}
