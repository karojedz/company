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
        given:
        Department department = new Department()

        and:
        departmentService.findDepartmentByName(DEPARTMENT_NAME) >> department

        when:
        employeeService.createNewEmployee(employeeForm)

        then:
        1 * employeeRepository.save(_)
    }

    def "should delete employee with given id and return message of success"() {
        given:
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
        employeeRepository.existsById(ID) >> true
        employeeRepository.getById(ID) >> employee
        employeeRepository.deleteById(ID) >> null

        expect:
        employeeService.deleteEmployeeById(ID) == "Employee with id=" + ID + " deleted successfully."
    }

    def "should return message of failure when trying to delete employee by id"() {
        given:
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
        employeeRepository.existsById(ID) >> false

        expect:
        employeeService.deleteEmployeeById(ID) == "There was no employee with id=" + ID + " to be found."
    }

    def "should update existing employee"() {
        given:
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

        and:
        employeeRepository.existsById(ID) >> true
        employeeRepository.getById(ID) >> employee
        departmentService.findDepartmentByName(DEPARTMENT_NAME) >> department

        when:
        employeeService.updateEmployee(ID, employeeForm)

        then:
        1 * employeeRepository.save(_)
    }

    def "should return error message after trying to update non-existent employee"() {
        given:
        employeeRepository.existsById(ID) >> false

        expect:
        employeeService.updateEmployee(ID, employeeForm) == "Employee with id=" + ID + " doesn't exist. \nYou should create it, not update."
    }

    def "should extract address from employeeForm"() {
        given:
        Address expectedAddress = new Address(ID, CITY_REGISTERED, STREET_REGISTERED, HOUSE_NR_REGISTERED,
                FLAT_NR_REGISTERED, CITY_RESIDENCE, STREET_RESIDENCE, HOUSE_NR_RESIDENCE, FLAT_NR_RESIDENCE,
                new Person())

        when:
        Address extracted = employeeService.extractAddressFromEmployeeForm(employeeForm)
        extracted.setPerson(new Person())
        extracted.setId(ID)

        then:
        extracted == expectedAddress
    }

    def "should extract person from employeeForm"() {
        given:
        Person expectedPerson = new Person(ID, FIRST_NAME, LAST_NAME, AGE, PESEL, new Address(), new Employee())

        when:
        Person extracted = employeeService.extractPersonFromEmployeeForm(employeeForm)
        extracted.setAddress(new Address())
        extracted.setEmployee(new Employee())
        extracted.setId(ID)

        then:
        extracted == expectedPerson
    }

    def "should create employee with employee form"() {
        given:
        Address address =  new Address(ID, CITY_REGISTERED, STREET_REGISTERED, HOUSE_NR_REGISTERED,
                FLAT_NR_REGISTERED, CITY_RESIDENCE, STREET_RESIDENCE, HOUSE_NR_RESIDENCE, FLAT_NR_RESIDENCE,
                new Person())
        Person person = new Person(ID, FIRST_NAME, LAST_NAME, AGE, PESEL, address, new Employee())
        address.setPerson(person)
        Department department = new Department()
        department.setId(ID)
        department.setDepartmentName(DEPARTMENT_NAME)
        Employee expectedEmployee = new Employee(ID, person, department, new BigDecimal(SALARY), POSITION)
        department.getEmployees().add(expectedEmployee)
        Department departmentFromEmployeeForm = new Department()
        departmentFromEmployeeForm.setDepartmentName(DEPARTMENT_NAME)
        departmentFromEmployeeForm.setId(ID)

        and:
        departmentService.findDepartmentByName(DEPARTMENT_NAME) >> departmentFromEmployeeForm

        when:
        Employee extracted = employeeService.createEmployeeWithEmployeeForm(employeeForm)
        extracted.setId(ID)
        extracted.getPerson().setId(ID)
        extracted.getPerson().getAddress().setId(ID)

        then:
        extracted == expectedEmployee
    }

    def "should update existing address with employeeForm"() {
        given:
        Address address = new Address()

        expect:
        employeeService.updateAddressWithEmployeeForm(address, employeeForm) == true
        address.getFlatNrResidence() == FLAT_NR_RESIDENCE
    }

    def "should update existing person with employeeForm"() {
        given:
        Person person = new Person()

        expect:
        employeeService.updatePersonWithEmployeeForm(person, employeeForm) == true
        person.getPESEL() == PESEL
    }

    def "should update existing employee with employeeForm"() {
        given:
        Employee employee = new Employee()

        expect:
        employeeService.updateEmployeeWithEmployeeForm(employee, employeeForm) == true
        employee.getSalary() == new BigDecimal(SALARY)
    }

    def "should create relationship between department and employee"() {
        given:
        Department department = new Department()
        Employee employee = new Employee()

        expect:
        employeeService.addDepartmentToEmployee(department, employee) == true
        employee.getDepartment() == department
        department.getEmployees().get(0) == employee
    }

    def "should update department for an employee"() {
        given:
        Department departmentOld = new Department()
        Employee employee = new Employee()
        departmentOld.getEmployees().add(employee)
        employee.setDepartment(departmentOld)
        Department departmentNew = new Department()

        when:
        employeeService.updateDepartment(departmentOld, departmentNew, employee)

        then:
        departmentOld.getEmployees().size() == 0
        departmentNew.getEmployees().get(0) == employee
        employee.getDepartment() == departmentNew
    }
}
