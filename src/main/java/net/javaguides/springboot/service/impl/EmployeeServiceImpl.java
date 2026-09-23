package net.javaguides.springboot.service.impl;

import net.javaguides.springboot.exception.ResourceExistsException;
import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmailIgnoreCase(employee.getEmail());
        if(savedEmployee.isPresent()){
            throw new ResourceExistsException("Employee with email " + employee.getEmail() + " already exists");
        }
                //.orElseThrow(() -> new ResourceNotFoundException("Employee with email " + employee.getEmail() + " not found"));
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
        return employee;
    }

    @Override
    public Optional<Employee> findEmployeeByIdWithLambda(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        return employeeRepository.findById(id)
                .map(upadtedEmployee -> employeeRepository.save(employee))
                .orElseThrow(() -> new ResourceNotFoundException("Employee with id " + id + " not found"));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.findById(id)
                .ifPresentOrElse(
                        deletedEmployee -> employeeRepository.deleteById(id),
                        () ->
                        {
                            throw new ResourceNotFoundException("Employee with id " + id + " not found");
                        }
                );
    }
}
