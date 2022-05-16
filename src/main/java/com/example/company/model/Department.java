package com.example.company.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Department that = (Department) other;
        return getId().equals(that.getId()) && getDepartmentName().equals(that.getDepartmentName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDepartmentName());
    }
}
