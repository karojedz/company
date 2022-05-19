package com.example.company.service;

import com.example.company.model.*;
import com.example.company.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AddressService addressService;
    private final DepartmentService departmentService;
    private final PersonService personService;

    public EmployeeService (EmployeeRepository employeeRepository, AddressService addressService,
                            DepartmentService departmentService, PersonService personService) {
        this.employeeRepository = employeeRepository;
        this.addressService = addressService;
        this.departmentService = departmentService;
        this.personService = personService;
    }

    public EmployeeForm createNewEmployee(EmployeeForm employeeForm) {
        Employee employee = createEmployeeWithEmployeeForm(employeeForm);
        employeeRepository.save(employee);
        return employeeForm;
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

    public EmployeeForm deleteEmployeeById(Long id) {
        Employee employee = employeeRepository.getById(id);
        EmployeeForm employeeForm = mapToEmployeeForm(employee);
        List<Employee> employees = employee.getDepartment().getEmployees();
        employees.remove(employee);
        employeeRepository.deleteById(id);
        return employeeForm;
    }

    public Object updateEmployee(Long id, EmployeeForm employeeForm) {
        Employee employee;
        Object employeeOrErrorMessage = findEmployeeWithId(id);
        if (employeeOrErrorMessage instanceof String) {
            return employeeOrErrorMessage;
        } else {
            employee = (Employee) employeeOrErrorMessage;
        }
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
        return employeeForm;
    }

    public Object findEmployeeWithId(Long id) {
        String message;
        try {
            Employee employee = getEmployeeById(id);
            return employee;
        } catch(IllegalArgumentException e) {
            message = e.getMessage();
        }
        return message;
    }

    private Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee with id=" + id + " doesn't exist. \nYou should create it, not update."));
    }

    private EmployeeForm mapToEmployeeForm(Employee employee) {
        EmployeeForm employeeForm = new EmployeeForm();
        fillEmployeeFormWithEmployee(employeeForm, employee);
        return employeeForm;
    }

    private boolean fillEmployeeFormWithEmployee(EmployeeForm employeeForm, Employee employee) {
        employeeForm.setSalary(employee.getSalary().toString());
        employeeForm.setPosition(employee.getPosition());
        fillEmployeeFormWithPerson(employeeForm, employee.getPerson());
        fillEmployeeFormWithAddress(employeeForm, employee.getPerson().getAddress());
        fillEmployeeFormWithDepartment(employeeForm, employee.getDepartment());
        return true;
    }

    private boolean fillEmployeeFormWithPerson(EmployeeForm employeeForm, Person person) {
        employeeForm.setFirstName(person.getFirstName());
        employeeForm.setLastName(person.getLastName());
        employeeForm.setAge(person.getAge());
        employeeForm.setPESEL(person.getPESEL());
        return true;
    }

    private boolean fillEmployeeFormWithAddress(EmployeeForm employeeForm, Address address) {
        employeeForm.setCityRegistered(address.getCityRegistered());
        employeeForm.setStreetRegistered(address.getStreetRegistered());
        employeeForm.setHouseNrRegistered(address.getHouseNrRegistered());
        employeeForm.setFlatNrRegistered(address.getFlatNrRegistered());

        //the optional address of residence:
        employeeForm.setCityResidence(address.getCityResidence());
        employeeForm.setStreetResidence(address.getStreetResidence());
        employeeForm.setHouseNrResidence(address.getHouseNrResidence());
        employeeForm.setFlatNrResidence(address.getFlatNrResidence());
        return true;
    }

    private boolean fillEmployeeFormWithDepartment(EmployeeForm employeeForm, Department department) {
        employeeForm.setDepartmentName(department.getDepartmentName());
        return true;
    }
}
