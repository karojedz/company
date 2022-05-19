package com.example.company.service

import com.example.company.model.Department
import com.example.company.repository.DepartmentRepository
import spock.lang.Specification

class DepartmentServiceTest extends Specification {

    private DepartmentService departmentService

    DepartmentRepository departmentRepository = Mock()

    def setup() {
        departmentService = new DepartmentService((departmentRepository))
    }

    def "should return Department from repository"() {
        when: "when findDepartmentByName is called"
        Department department = new Department()
        department.setId(6)
        department.setDepartmentName("name")
        departmentRepository.getDepartment("name") >> department
        departmentService.findDepartmentByName("name")

        then: "should call getDepartment in departmentRepository"
        1 * departmentRepository.getDepartment(_)
    }
}
