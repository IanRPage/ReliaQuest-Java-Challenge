package com.challenge.api.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.challenge.api.model.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("EmployeeController Tests")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/employee - Should return empty list initially")
    void testGetAllEmployeesEmpty() throws Exception {
        mockMvc.perform(get("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("GET /api/v1/employee - Should return created employees")
    void testGetAllEmployees() throws Exception {
        // Create first employee
        EmployeeDTO emp1 = new EmployeeDTO();
        emp1.setFirstName("John");
        emp1.setLastName("Doe");
        emp1.setSalary(50000);
        emp1.setAge(30);
        emp1.setJobTitle("Engineer");
        emp1.setEmail("john@example.com");

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp1)))
                .andExpect(status().isOk());

        // Create second employee
        EmployeeDTO emp2 = new EmployeeDTO();
        emp2.setFirstName("Jane");
        emp2.setLastName("Smith");
        emp2.setSalary(55000);
        emp2.setAge(28);
        emp2.setJobTitle("Developer");
        emp2.setEmail("jane@example.com");

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emp2)))
                .andExpect(status().isOk());

        // Get all employees
        mockMvc.perform(get("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("John", "Jane")))
                .andExpect(jsonPath("$[*].lastName", containsInAnyOrder("Doe", "Smith")));
    }

    @Test
    @DisplayName("GET /api/v1/employee/{uuid} - Should return 404 for non-existent UUID")
    void testGetEmployeeByUuidNotFound() throws Exception {
        String nonExistentUuid = "12345678-1234-1234-1234-123456789012";

        mockMvc.perform(get("/api/v1/employee/{uuid}", nonExistentUuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should create employee with all required fields")
    void testCreateEmployee() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSalary(50000);
        dto.setAge(30);
        dto.setJobTitle("Engineer");
        dto.setEmail("john@example.com");

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.lastName", equalTo("Doe")))
                .andExpect(jsonPath("$.salary", equalTo(50000)))
                .andExpect(jsonPath("$.age", equalTo(30)))
                .andExpect(jsonPath("$.jobTitle", equalTo("Engineer")))
                .andExpect(jsonPath("$.email", equalTo("john@example.com")))
                .andExpect(jsonPath("$.uuid", notNullValue()))
                .andExpect(jsonPath("$.contractHireDate", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should use current time as hire date if not provided")
    void testCreateEmployeeDefaultHireDate() throws Exception {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSalary(50000);
        dto.setAge(30);
        dto.setJobTitle("Engineer");
        dto.setEmail("john@example.com");

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractHireDate", notNullValue()));
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should use provided hire date")
    void testCreateEmployeeWithProvidedHireDate() throws Exception {
        Instant hireDate = Instant.parse("2024-01-01T00:00:00Z");
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSalary(50000);
        dto.setAge(30);
        dto.setJobTitle("Engineer");
        dto.setEmail("john@example.com");
        dto.setContractHireDate(hireDate);

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contractHireDate", containsString("2024-01-01")));
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when firstName is null")
    void testCreateEmployeeMissingFirstName() throws Exception {
        String jsonPayload = "{\"lastName\":\"Doe\",\"salary\":50000,\"age\":30,\"jobTitle\":\"Engineer\",\"email\":\"john@example.com\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when lastName is null")
    void testCreateEmployeeMissingLastName() throws Exception {
        String jsonPayload = "{\"firstName\":\"John\",\"salary\":50000,\"age\":30,\"jobTitle\":\"Engineer\",\"email\":\"john@example.com\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when salary is null")
    void testCreateEmployeeMissingSalary() throws Exception {
        String jsonPayload = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"age\":30,\"jobTitle\":\"Engineer\",\"email\":\"john@example.com\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when age is null")
    void testCreateEmployeeMissingAge() throws Exception {
        String jsonPayload = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"salary\":50000,\"jobTitle\":\"Engineer\",\"email\":\"john@example.com\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when jobTitle is null")
    void testCreateEmployeeMissingJobTitle() throws Exception {
        String jsonPayload = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"salary\":50000,\"age\":30,\"email\":\"john@example.com\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when email is null")
    void testCreateEmployeeMissingEmail() throws Exception {
        String jsonPayload = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"salary\":50000,\"age\":30,\"jobTitle\":\"Engineer\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/employee - Should return 400 when multiple required fields are null")
    void testCreateEmployeeMissingMultipleFields() throws Exception {
        String jsonPayload = "{\"firstName\":\"John\",\"age\":30,\"jobTitle\":\"Engineer\"}";

        mockMvc.perform(post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isBadRequest());
    }
}

