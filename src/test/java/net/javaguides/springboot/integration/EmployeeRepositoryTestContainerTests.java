package net.javaguides.springboot.integration;

import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.EmployeeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryTestContainerTests extends AbstractContainerBaseTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    @BeforeEach
    public void setup() {
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test").build();
    }

    //JUnit test for save employee operation
    @Test
    @DisplayName("JUnit test for Save Employee Operation")
    public void givenEmployeeObject_whenSave_ThenReturnSavedEmployee() {

        //given - setup employee object
//        Employee employee = Employee.builder()
//                .firstName("Anaya")
//                .lastName("Banerjee")
//                .email("test").build();

        //when - save the employee object
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();

        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    //Junit test for get all the employees
    @Test
    @DisplayName("Junit test for get all the employees")
    public void givenListOfEmployeeSavedInDb_whenFindAlltheEmployees_thenReturnsListOfEmployee() {

        //given - precondition setup

//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();

        Employee employee2 = Employee.builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test1").build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when - action or the behavior we are going to test
        List<Employee> employeeList = employeeRepository.findAll();

        //then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    //Junit test for get employee by id operation
    @Test
    @DisplayName("Junit test for get employee by id operation")
    public void givenEmployeeObject_whenFindById_thenReturnId() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();

        Employee employee2 = Employee.builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test1").build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElse(null);

        //then - verify the output
        Assertions.assertThat(savedEmployee.getId()).isEqualTo(employee.getId());
    }

    //Junit test for find employee by email operation
    @Test
    @DisplayName("Junit test for find employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);
        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findByEmailIgnoreCase(employee.getEmail()).get();

        //then - verify the output
        Assertions.assertThat(savedEmployee.getEmail()).isEqualTo(employee.getEmail());
    }

    //Junit test for update employee operation
    @Test
    @DisplayName("Junit test for update employee operation")
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).orElse(null);
        savedEmployee.setEmail("test2");
        savedEmployee.setLastName("Singh");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then - verify the output

        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo(savedEmployee.getEmail());
    }

    //Junit test to delete employee operation
    @Test
    @DisplayName("Junit test to delete employee operation")
    public void givenEmployeeObject_whenDelete_thenRemovedEmployee() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);

        //when - action or the behavior we are going to test
        //employeeRepository.delete(employee1);
        employeeRepository.deleteById(employee.getId());
        Employee deletedEmployee = employeeRepository.findById(employee.getId()).orElse(null);

        //then - verify the output
        Assertions.assertThat(deletedEmployee).isNull();
    }

    //Junit test for custom query using JPQL with index
    @Test
    @DisplayName("Junit test for custom query using JPQL with index")
    public void givenFirstNameAndLastName_whenFindByJpql_thenReturnEmployeeObject() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findByJpqlIndexParam(employee.getFirstName(), employee.getEmail());

        //then - verify the output
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }

    //Junit test for custom query using JPQL with named param
    @Test
    @DisplayName("Junit test for custom query using JPQL with named param")
    public void givenFirstNameAndLastName_whenFindByJpqlNamedParam_thenReturnEmployeeObject() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findByJpqlNamedParameters(employee.getFirstName(), employee.getEmail());

        //then - verify the output
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }

    //Junit test for custom query using native sql with index param
    @Test
    @DisplayName("Junit test for custom query using native sql with index param")
    public void givenFirstNameAndLastName_whenFindByNativeSqlIndexParam_thenReturnEmployeeObject() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findByNativeQueryIndexParameters(employee.getFirstName(), employee.getEmail());

        //then - verify the output
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }

    //Junit test for custom query using native sql with named param
    @Test
    @DisplayName("Junit test for custom query using native sql with named param")
    public void givenFirstNameAndLastName_whenFindByNativeSqlNamedParam_thenReturnEmployeeObject() {

        //given - precondition setup
//        Employee employee1 = Employee.builder()
//                .firstName("Test")
//                .lastName("Test")
//                .email("test").build();
        employeeRepository.save(employee);

        //when - action or the behavior we are going to test
        Employee savedEmployee = employeeRepository.findByNativeQueryNamedParameters(employee.getFirstName(), employee.getEmail());

        //then - verify the output
        Assertions.assertThat(savedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
    }
}
