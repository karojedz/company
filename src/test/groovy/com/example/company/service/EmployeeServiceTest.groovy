package com.example.company.service

import com.example.company.model.Address
import com.example.company.model.Department
import com.example.company.model.EmployeeForm
import com.example.company.repository.EmployeeRepository
import spock.lang.Specification

class EmployeeServiceTest extends Specification{

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
        departmentService.findDepartmentByName("Department") >> department
        employeeService.createNewEmployee(employeeForm)

        then:
        1 * employeeRepository.save(_)
    }

    def "test deleteEmployeeById"() {
    }

    def "test updateEmployee"() {
    }

    def "should extract Address from EmployeeForm"() {
        when:
        Address address = employeeService.extractAddressFromEmployeeForm(employeeForm)

    }


}
