package com.example.company.service;

import com.example.company.model.*;
import com.example.company.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AddressService addressService;
    private final DepartmentService departmentService;
    private final PersonService personService;

    public Employee createNewEmployee(EmployeeForm employeeForm) {
        Employee employee = createEmployeeWithEmployeeForm(employeeForm);
        return employeeRepository.save(employee);
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

    private Employee createEmployeeWithEmployeeForm(EmployeeForm employeeForm) {
        Employee employee = new Employee();
        updateEmployeeWithEmployeeForm(employee, employeeForm);
        Address address = extractAddressFromEmployeeForm(employeeForm);
        Person person = extractPersonFromEmployeeForm(employeeForm);
        person.setAddress(address);
        address.setPerson(person);
        employee.setPerson(person);
        person.setEmployee(employee);
        Department department = departmentService.findDepartmentByName(employeeForm.getDepartmentName());
        addDepartmentToEmployee(department, employee);
        return employee;
    }

    private boolean updateAddressWithEmployeeForm(Address address, EmployeeForm employeeForm) {
        address.setCityRegistered(employeeForm.getCityRegistered());
        address.setStreetRegistered(employeeForm.getStreetRegistered());
        address.setHouseNrRegistered(employeeForm.getHouseNrRegistered());
        address.setFlatNrRegistered(employeeForm.getFlatNrRegistered());
        //the optional address of residence:
        address.setCityResidence(employeeForm.getCityResidence());
        address.setStreetResidence(employeeForm.getStreetResidence());
        address.setHouseNrResidence(employeeForm.getHouseNrResidence());
        address.setFlatNrResidence(employeeForm.getFlatNrResidence());
        return true;
    }

    private boolean updatePersonWithEmployeeForm(Person person, EmployeeForm employeeForm) {
        person.setFirstName(employeeForm.getFirstName());
        person.setLastName(employeeForm.getLastName());
        person.setAge(employeeForm.getAge());
        person.setPESEL(employeeForm.getPESEL());
        return true;
    }

    private boolean updateEmployeeWithEmployeeForm(Employee employee, EmployeeForm employeeForm) {
        employee.setPosition(employeeForm.getPosition());
        employee.setSalary(new BigDecimal(employeeForm.getSalary()));
        return true;
    }

    private boolean addDepartmentToEmployee(Department department, Employee employee) {
        employee.setDepartment(department);
        department.getEmployees().add(employee);
        return true;
    }

    private boolean updateDepartment(Department departmentOld, Department departmentNew, Employee employee) {
        departmentOld.getEmployees().remove(employee);
        addDepartmentToEmployee(departmentNew, employee);
        return true;
    }

    public String deleteEmployeeById(Long id) {
        if (employeeRepository.existsById(id)) {
            Employee employee = employeeRepository.getById(id);
            List<Employee> employees = employee.getDepartment().getEmployees();
            employees.remove(employee);
            employeeRepository.deleteById(id);
            return "Employee with id=" + id + " deleted successfully.";
        }
        return "There was no employee with id=" + id + " to be found.";
    }

    public String updateEmployee(Long id, EmployeeForm employeeForm) {
        if (employeeRepository.existsById(id)) {
            Employee employee = employeeRepository.getById(id);
            Person person = employee.getPerson();
            Address address = employee.getPerson().getAddress();
            Department departmentOld = employee.getDepartment();
            Department department = departmentService.findDepartmentByName(employeeForm.getDepartmentName());
            if (departmentOld!=department) {
                updateDepartment(departmentOld, department, employee);
            }
            updateAddressWithEmployeeForm(address, employeeForm);
            updatePersonWithEmployeeForm(person, employeeForm);
            updateEmployeeWithEmployeeForm(employee, employeeForm);
            employeeRepository.save(employee);
            return "Employee with id=" + id + " updated";
        } else {
            return "Employee with id=" + id + " doesn't exist. \nYou should create it, not update.";
        }
    }
}
