package com.example.company.service

import com.example.company.model.Address
import com.example.company.model.Department
import com.example.company.model.Employee
import com.example.company.model.EmployeeForm
import com.example.company.model.Person
import com.example.company.repository.EmployeeRepository
import spock.lang.Specification

class EmployeeServiceTest extends Specification{

    def ID = 1L
    def FIRST_NAME = "name"
    def LAST_NAME = "surname"
    def AGE = 25
    def PESEL = "PESEL"
    def CITY_REGISTERED = "cityRegistered"
    def STREET_REGISTERED = "streetRegistered"
    def HOUSE_NR_REGISTERED = "houseNrRegistered"
    def FLAT_NR_REGISTERED = "flatNrRegistered"
    def CITY_RESIDENCE = "cityResidence"
    def STREET_RESIDENCE = "streetResidence"
    def HOUSE_NR_RESIDENCE = "houseNrResidence"
    def FLAT_NR_RESIDENCE = "flatNrResidence"
    def DEPARTMENT_NAME = "departmentName"
    def SALARY = "7777"
    def POSITION = "position"

    def employeeForm = new EmployeeForm(FIRST_NAME, LAST_NAME, AGE, PESEL, CITY_REGISTERED, STREET_REGISTERED,
            HOUSE_NR_REGISTERED, FLAT_NR_REGISTERED, CITY_RESIDENCE, STREET_RESIDENCE, HOUSE_NR_RESIDENCE,
            FLAT_NR_RESIDENCE, DEPARTMENT_NAME, SALARY, POSITION)

    private EmployeeService employeeService

    EmployeeRepository employeeRepository = Mock()
    AddressService addressService = Mock()
    DepartmentService departmentService = Mock()
    PersonService personService = Mock()

    def setup() {
        employeeService = new EmployeeService(employeeRepository, addressService, departmentService, personService)
    }

    def "employeeService should be created"() {
        expect:
        employeeService
    }

    def "should save new employee"() {
        when:
        Department department = new Department()
        departmentService.findDepartmentByName(DEPARTMENT_NAME) >> department
        employeeService.createNewEmployee(employeeForm)

        then:
        1 * employeeRepository.save(_)
    }

    def "should delete employee with given id"() {
        when:
        Employee employee = new Employee()
        Person person = new Person()
        person.setAddress(new Address())
        employee.setPerson(person)
        employee.setId(ID)
        employee.setPosition(POSITION)
        employee.setSalary(new BigDecimal(SALARY))
        Department department = new Department()
        department.getEmployees().add(employee)
        employee.setDepartment(department)

        and:
        employeeRepository.getById(ID) >> employee
        employeeRepository.deleteById(ID) >>
        employeeService.deleteEmployeeById(ID)

        then:
        1 * employeeRepository.deleteById(ID)
    }

    def "should update existing employee"() {
        when:
        Employee employee = new Employee()
        Person person = new Person()
        person.setAddress(new Address())
        employee.setPerson(person)
        employee.setId(ID)
        employee.setPosition(POSITION)
        employee.setSalary(new BigDecimal(SALARY))
        Department department = new Department()
        department.setDepartmentName(DEPARTMENT_NAME)
        department.getEmployees().add(employee)
        employee.setDepartment(department)

        employee.setPerson(person)

        employeeRepository.findById(ID) >> Optional.of(employee)
        departmentService.findDepartmentByName(DEPARTMENT_NAME) >> department
        employeeService.updateEmployee(ID, employeeForm)

        then:
        1 * employeeRepository.save(_)
    }

    def "should return error message after trying to update non-existent employee"() {
        when:
        employeeRepository.findById(ID) >> Optional.empty()

        then:
        employeeService.updateEmployee(ID, employeeForm) == "Employee with id=" + ID + " doesn't exist. \nYou should create it, not update."
    }

    def "are private methods called from here? They are"() {
        when:
        Department department = new Department()
        departmentService.findDepartmentByName(DEPARTMENT_NAME) >> department
        Employee employee = employeeService.createEmployeeWithEmployeeForm(employeeForm)

        then:
        employee.getPosition() == POSITION
    }
}
