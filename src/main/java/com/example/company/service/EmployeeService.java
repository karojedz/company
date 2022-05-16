package com.example.company.service;

import com.example.company.model.*;
import com.example.company.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private AddressService addressService;
    private DepartmentService departmentService;
    private PersonService personService;

    @Autowired
    public EmployeeService (EmployeeRepository employeeRepository, AddressService addressService,
                            DepartmentService departmentService, PersonService personService) {
        this.employeeRepository = employeeRepository;
        this.addressService = addressService;
        this.departmentService = departmentService;
        this.personService = personService;
    }

    public ResponseEntity<String> createNewEmployee(EmployeeForm employeeForm) {
        Address address = extractAddressFromEmployeeForm(employeeForm);
        Person person = extractPersonFromEmployeeForm(employeeForm);
        person.setAddress(address);
        address.setPerson(person);

        Employee employee = extractEmployeeFromEmployeeForm(employeeForm);
        employee.setPerson(person);
        person.setEmployee(employee);
        Department department = departmentService.findDepartmentByName(employeeForm.getDepartmentName());
        addDepartmentToEmployee(department, employee);
        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully.");
    }

    private Address extractAddressFromEmployeeForm(EmployeeForm employeeForm) {
        Address address = new Address();
        updateAddressWithEmployeeForm(address, employeeForm);
        return address;
    }

    private Person extractPersonFromEmployeeForm(EmployeeForm employeeForm) {
        Person person = new Person();
        updatePersonWithEmployeeForm(person, employeeForm);
        return person;
    }

    private Employee extractEmployeeFromEmployeeForm(EmployeeForm employeeForm) {
        Employee employee = new Employee();
        updateEmployeeWithEmployeeForm(employee, employeeForm);
        return employee;
    }

    private void updateAddressWithEmployeeForm(Address address, EmployeeForm employeeForm) {
        address.setCityRegistered(employeeForm.getCityRegistered());
        address.setStreetRegistered(employeeForm.getStreetRegistered());
        address.setHouseNrRegistered(employeeForm.getHouseNrRegistered());
        address.setFlatNrRegistered(employeeForm.getFlatNrRegistered());
        //the optional address of residence:
        address.setCityResidence(employeeForm.getCityResidence());
        address.setStreetResidence(employeeForm.getStreetResidence());
        address.setHouseNrResidence(employeeForm.getHouseNrResidence());
        address.setFlatNrResidence(employeeForm.getFlatNrResidence());
    }

    private void updatePersonWithEmployeeForm(Person person, EmployeeForm employeeForm) {
        person.setFirstName(employeeForm.getFirstName());
        person.setLastName(employeeForm.getLastName());
        person.setAge(employeeForm.getAge());
        person.setPESEL(employeeForm.getPESEL());
    }

    private void updateEmployeeWithEmployeeForm(Employee employee, EmployeeForm employeeForm) {
        employee.setPosition(employeeForm.getPosition());
        employee.setSalary(new BigDecimal(employeeForm.getSalary()));
    }

    private void addDepartmentToEmployee(Department department, Employee employee) {
        employee.setDepartment(department);
        department.getEmployees().add(employee);
    }

    private void updateDepartment(Department departmentOld, Department departmentNew, Employee employee) {
        departmentOld.getEmployees().remove(employee);
        departmentNew.getEmployees().add(employee);
        employee.setDepartment(departmentNew);
    }

    public ResponseEntity deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.getById(id);
        List<Employee> employees = employee.getDepartment().getEmployees();
        employees.remove(employee);
        employeeRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    public ResponseEntity<String> updateEmployee(Long id, EmployeeForm employeeForm) {
        Optional<Employee> foundEmployee = employeeRepository.findById(id);
        Employee employee;
        Person person;
        Address address;
        Department department = departmentService.findDepartmentByName(employeeForm.getDepartmentName());
        ResponseEntity<String> responseEntity;
        if (foundEmployee.isPresent()) {
            employee = foundEmployee.get();
            person = employee.getPerson();
            address = person.getAddress();
            Department departmentOld = employee.getDepartment();
            if (departmentOld!=department) {
                updateDepartment(departmentOld, department, employee);
            }
            responseEntity = ResponseEntity.status(HttpStatus.OK).body("Employee updated.");
        } else {
            employeeRepository.insertNewEmployee(id);
            employee = employeeRepository.findById(id).get();
            person = new Person();
            address = new Address();
            employee.setPerson(person);
            person.setEmployee(employee);
            person.setAddress(address);
            address.setPerson(person);
            addDepartmentToEmployee(department, employee);
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body("Employee not found. New employee created.");
        }
        updateAddressWithEmployeeForm(address, employeeForm);
        updatePersonWithEmployeeForm(person, employeeForm);
        updateEmployeeWithEmployeeForm(employee, employeeForm);
        employeeRepository.save(employee);
        return responseEntity;
    }
}
