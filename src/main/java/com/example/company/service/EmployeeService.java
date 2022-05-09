package com.example.company.service;

import com.example.company.model.*;
import com.example.company.repository.AddressRepository;
import com.example.company.repository.DepartmentRepository;
import com.example.company.repository.EmployeeRepository;
import com.example.company.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.apache.catalina.security.SecurityUtil.remove;

@Service
public class EmployeeService {

    private AddressRepository addressRepository;
    private DepartmentRepository departmentRepository;
    private EmployeeRepository employeeRepository;
    private PersonRepository personRepository;

    @Autowired
    public EmployeeService (AddressRepository addressRepository, DepartmentRepository departmentRepository,
                            EmployeeRepository employeeRepository, PersonRepository personRepository) {
        this.addressRepository = addressRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.personRepository = personRepository;
    }

    public void createNewEmployee(EmployeeForm employeeForm) {
        Address address = extractAddressFromEmployeeForm(employeeForm);

        Person person = extractPersonFromEmployeeForm(employeeForm);
        person.setAddress(address);

        Employee employee = extractEmployeeFromEmployeeForm(employeeForm);
        employee.setPerson(person);

        employeeRepository.save(employee);
    }

    private Address extractAddressFromEmployeeForm(EmployeeForm employeeForm) {
        Address address = new Address();
        address.setCityRegistered(employeeForm.getCityRegistered());
        address.setStreetRegistered(employeeForm.getStreetRegistered());
        address.setHouseNrRegistered(employeeForm.getHouseNrRegistered());
        address.setFlatNrRegistered(employeeForm.getFlatNrRegistered());

        address.setCityResidence(employeeForm.getCityResidence());
        address.setStreetResidence(employeeForm.getStreetResidence());
        address.setHouseNrResidence(employeeForm.getHouseNrResidence());
        address.setFlatNrResidence(employeeForm.getFlatNrResidence());
        return address;
    }

    private Person extractPersonFromEmployeeForm(EmployeeForm employeeForm) {
        Person person = new Person();
        person.setFirstName(employeeForm.getFirstName());
        person.setLastName(employeeForm.getLastName());
        person.setAge(employeeForm.getAge());
        person.setPESEL(employeeForm.getPESEL());
        return person;
    }

    private Employee extractEmployeeFromEmployeeForm(EmployeeForm employeeForm) {
        Employee employee = new Employee();
        employee.setPosition(employeeForm.getPosition());
        employee.setSalary(new BigDecimal(employeeForm.getSalary()));
        employee.setDepartment(departmentRepository.getDepartment(employeeForm.getDepartmentName()));
        return employee;
    }

    public void deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.getById(id);
        List<Employee> employees = employee.getDepartment().getEmployees();
        employees.remove(employee);
        employeeRepository.deleteById(id);
    }

    public void updateEmployee(Long id, EmployeeForm employeeForm) {
        Address address = extractAddressFromEmployeeForm(employeeForm);

        Person person = extractPersonFromEmployeeForm(employeeForm);
        person.setAddress(address);

        Employee employee = extractEmployeeFromEmployeeForm(employeeForm);
        employee.setPerson(person);
        employee.setId(id);
        employeeRepository.save(employee);
    }
}
