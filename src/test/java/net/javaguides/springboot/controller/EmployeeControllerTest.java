package net.javaguides.springboot.controller;


import net.javaguides.springboot.exception.ResourceNotFoundException;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc; //Main entry point for server-side Spring MVC test support. Need this dependency to call rest apis

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private JsonMapper jsonMapper;


    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();
    }

    //Junit test for create employee rest api
    @Test
    @DisplayName("Junit test for create employee rest api")
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition setup
        given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behavior we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees/create-employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(employee)));

        //then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));

    }

    //Junit test for get all employees REST API
    @Test
    @DisplayName("Junit test for get all employees REST API")
    public void givenListOfEmployees_whenGetAllEmployee_thenReturnEmployeeList() throws Exception {

        //given - precondition setup

        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build());
        listOfEmployees.add(Employee.builder()
                .firstName("Anaisha")
                .lastName("Singh")
                .email("test1")
                .build());

        given(employeeService.findAllEmployees()).willReturn(listOfEmployees);


        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/employees/get-all-employees")
                .contentType(MediaType.APPLICATION_JSON));

        //then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", CoreMatchers.is(listOfEmployees.size())));
    }

    @Test
    @DisplayName("Junit test for get employee by id REST API")
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();


        given(employeeService.findEmployeeById(employeeId)).willReturn(employee);


        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/employees/get-employee/{id}", employeeId));


        //then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath(("$.firstName"), CoreMatchers.is(employee.getFirstName())));
    }

    @Test
    @DisplayName("Junit test case for negative scenario, when employee is not found")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId)).willThrow(new ResourceNotFoundException("Employee with id " + employeeId + " not found"));

        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/employees/get-employee/{id}", employeeId));

        //Then - Verufy Output
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Junit test for get employee by id REST API")
    public void givenEmployeeId_whenGetEmployeeByIdLamdaMethod_thenReturnEmployee() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();


        given(employeeService.findEmployeeByIdWithLambda(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/employees/get-employee-lambda/{id}", employeeId));


        //then - verify the output
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath(("$.firstName"), CoreMatchers.is(employee.getFirstName())));
    }

    @Test
    @DisplayName("Junit test case for negative scenario, when employee is not found")
    public void givenInvalidEmployeeId_whenGetEmployeeByIdWithLambda_thenReturnEmpty() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        given(employeeService.findEmployeeByIdWithLambda(employeeId)).willReturn(Optional.empty());

        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(get("/api/employees/get-employee-lambda/{id}", employeeId));

        //Then - Verufy Output
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Junit test for update employee REST API")
    public void givenEmployee_whenUpdateEmployee_thenReturnEmployee() throws Exception {
        //given - precondition setup
        long employeeId = 1L;


        Employee savedEmployee = Employee
                .builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();

        Employee updatedEmployee = Employee
                .builder()
                .firstName("Anayaa")
                .lastName("Banerjee")
                .email("test1")
                .build();

        given(employeeService.findEmployeeById(employeeId)).willReturn(savedEmployee);
        //given(employeeService.updateEmployee(eq(employeeId),ArgumentMatchers.any(Employee.class))).willAnswer((invocation) -> invocation.getArgument(0));
        given(employeeService.updateEmployee(eq(employeeId),
                ArgumentMatchers.any(Employee.class))).willReturn(updatedEmployee);


        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(put("/api/employees/update-employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updatedEmployee)));

        //Then - Verufy Output
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())));
    }

    @Test
    @DisplayName("Junit test for negative update employee REST API")
    public void givenInvalidEmployee_whenUpdateEmployee_thenReturnNotFoundException() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        Employee savedEmployee = Employee
                .builder()
                .firstName("Anaya")
                .lastName("Banerjee")
                .email("test")
                .build();

        Employee updatedEmployee = Employee
                .builder()
                .firstName("Anayaa")
                .lastName("Banerjee")
                .email("test1")
                .build();


        given(employeeService.findEmployeeById(employeeId)).willReturn(null);
        given(employeeService.updateEmployee(eq(employeeId),
                ArgumentMatchers.any(Employee.class))).willThrow(new ResourceNotFoundException("Employee with id " + employeeId + " not found"));

        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(put("/api/employees/update-employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updatedEmployee)));

        //Then - Verufy Output
        resultActions.andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Junit test for delete employee REST API")
    public void givenEmployeeId_whenDeleteEmployee_thenReturn204() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId)).willReturn(employee);
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(delete("/api/employees/delete-employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //Then - Verify Output
        resultActions.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Junit test for negative delete employee REST API when employee does not exists")
    public void givenEmployeeIdDoesNotExists_whenDeleteEmployee_thenReturn204() throws Exception {
        //given - precondition setup
        long employeeId = 1L;
        given(employeeService.findEmployeeById(employeeId)).willThrow(new ResourceNotFoundException("Employee with id " + employeeId + " not found"));


        //when - action or the behavior we are going to test
        ResultActions resultActions = mockMvc.perform(delete("/api/employees/delete-employee/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //Then - Verify Output
        resultActions.andDo(print())
                .andExpect(status().isNoContent());
    }
}
