package net.javaguides.springboot.service;

import net.javaguides.springboot.exception.ResourceExistsException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import net.javaguides.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    //private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();
    }

    //Junit test for save employee method
    @Test
    @DisplayName("Junit test for save employee method")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployee() {

        //given - precondition setup

        given(employeeRepository.findByEmailIgnoreCase(employee.getEmail())).willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //Junit test for ResourceExistsException when employee object exists in DB
    @Test
    @DisplayName("Junit test for ResourceExistsException when employee object exists in DB")
    public void givenEmployeeObjectHasEmail_whenSaveEmployee_thenThrowResourceExistsException() {

        //given - precondition setup
        given(employeeRepository.findByEmailIgnoreCase(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behavior we are going to test
        assertThrows(ResourceExistsException.class, () -> employeeService.saveEmployee(employee));

        //then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //Junit test to find all employees
    @Test
    @DisplayName("Junit test to find all employees")
    public void givenEmployeesSavedinDb_whenFindAllEmployees_thenReturnListOfEmployees() {

        //given - precondition setup

        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or the behavior we are going to test
        List<Employee> employeeList = employeeService.findAllEmployees();

        //then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(2);
    }

    //Junit test to find empty employee list
    @Test
    @DisplayName("Junit test to find empty employee list")
    public void givenEmptyEmployeeDb_whenFindAllEmployees_thenReturnEmptyEmployeeList() {

        //given - precondition setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behavior we are going to test
        List<Employee> employeeList = employeeService.findAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList).hasSize(0);
    }

    //Junit test for get employee by id
    @Test
    @DisplayName("Junit test for get employee by id")
    public void givenEmployeeId_whenFindEmployeeById_thenReturnEmployee() {

        //given - precondition setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behavior we are going to test
        Employee employeeData = employeeService.findEmployeeById(employee.getId());

        //then - verify the output
        assertThat(employeeData).isNotNull();
    }

    //Junit test for update employee
        @Test
        @DisplayName("Junit test for update employee")
        public void givenEmployee_whenUpdateEmployee_thenReturnEmployee() {

            //given - precondition setup
            long employeeId = employee.getId();
            Employee employee1 = Employee.builder()
                    .id(1L)
                    .firstName("Anaya")
                    .lastName("Singh")
                    .email("test1")
                    .build();
            given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
            given(employeeRepository.save(employee1)).willReturn(employee1);
            //when - action or the behavior we are going to test
            Employee savedEmployee = employeeService.updateEmployee(employeeId,employee1);
            //then - verify the output
            assertThat(savedEmployee).isNotNull();
        }

    //Junit test for delete employee
    @Test
    @DisplayName("Junit test for delete employee")
    public void givenEmployee_whenDeleteEmployee_thenReturnNothing() {

        //given - precondition setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
        willDoNothing().given(employeeRepository).deleteById(1L);

        //when - action or the behavior we are going to test
        employeeService.deleteEmployee(1L);
        //then - verify the output
        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
