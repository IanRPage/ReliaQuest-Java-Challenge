package com.challenge.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("EmployeeService Tests")
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("Should return empty or existing list when service is fresh")
    void testGetAllEmployeesInitially() {
        List<Employee> employees = employeeService.getAllEmployees();
        assertNotNull(employees);
        assertTrue(employees.isEmpty());
    }

    @Test
    @DisplayName("Should create employee and return it")
    void testCreateEmployee() {
        EmployeeDTO dto = createTestEmployeeDTO("John", "Doe", 50000, 30, "Engineer", "john@example.com");
        Employee created = employeeService.createEmployee(dto);

        assertNotNull(created);
        assertEquals("John", created.getFirstName());
        assertEquals("Doe", created.getLastName());
        assertEquals(50000, created.getSalary());
        assertEquals(30, created.getAge());
        assertEquals("Engineer", created.getJobTitle());
        assertEquals("john@example.com", created.getEmail());
        assertNotNull(created.getUuid());
    }

    @Test
    @DisplayName("Should assign UUID to created employee")
    void testCreateEmployeeAssignsUuid() {
        EmployeeDTO dto = createTestEmployeeDTO("Jane", "Smith", 55000, 28, "Developer", "jane@example.com");
        Employee created = employeeService.createEmployee(dto);

        assertNotNull(created.getUuid());
    }

    @Test
    @DisplayName("Should use provided hire date")
    void testCreateEmployeeWithProvidedHireDate() {
        Instant hireDate = Instant.parse("2024-01-01T00:00:00Z");
        EmployeeDTO dto = createTestEmployeeDTO("John", "Doe", 50000, 30, "Engineer", "john@example.com");
        dto.setContractHireDate(hireDate);

        Employee created = employeeService.createEmployee(dto);
        assertEquals(hireDate, created.getContractHireDate());
    }

    @Test
    @DisplayName("Should retrieve employee by UUID")
    void testGetEmployeeByUuid() {
        EmployeeDTO dto = createTestEmployeeDTO("John", "Doe", 50000, 30, "Engineer", "john@example.com");
        Employee created = employeeService.createEmployee(dto);
        UUID uuid = created.getUuid();

        Employee retrieved = employeeService.getEmployeeByUuid(uuid);
        assertNotNull(retrieved);
        assertEquals(uuid, retrieved.getUuid());
        assertEquals("John", retrieved.getFirstName());
    }

    @Test
    @DisplayName("Should return null for non-existent UUID")
    void testGetEmployeeByUuidNotFound() {
        UUID nonExistentUuid = UUID.randomUUID();
        Employee retrieved = employeeService.getEmployeeByUuid(nonExistentUuid);
        assertNull(retrieved);
    }

    @Test
    @DisplayName("Should return all created employees")
    void testGetAllEmployees() {
        EmployeeDTO dto1 = createTestEmployeeDTO("John", "Doe", 50000, 30, "Engineer", "john@example.com");
        EmployeeDTO dto2 = createTestEmployeeDTO("Jane", "Smith", 55000, 28, "Developer", "jane@example.com");
        EmployeeDTO dto3 = createTestEmployeeDTO("Bob", "Johnson", 60000, 35, "Manager", "bob@example.com");

        employeeService.createEmployee(dto1);
        employeeService.createEmployee(dto2);
        employeeService.createEmployee(dto3);

        List<Employee> employees = employeeService.getAllEmployees();
        assertEquals(3, employees.size());
    }

    @Test
    @DisplayName("Should create multiple employees with different UUIDs")
    void testCreateMultipleEmployeesUnique() {
        EmployeeDTO dto1 = createTestEmployeeDTO("John", "Doe", 50000, 30, "Engineer", "john@example.com");
        EmployeeDTO dto2 = createTestEmployeeDTO("Jane", "Smith", 55000, 28, "Developer", "jane@example.com");

        Employee emp1 = employeeService.createEmployee(dto1);
        Employee emp2 = employeeService.createEmployee(dto2);

        assertNotEquals(emp1.getUuid(), emp2.getUuid());
    }

    @Test
    @DisplayName("Should persist employee in internal map")
    void testEmployeePersistence() {
        EmployeeDTO dto = createTestEmployeeDTO("John", "Doe", 50000, 30, "Engineer", "john@example.com");
        Employee created = employeeService.createEmployee(dto);
        UUID uuid = created.getUuid();

        Employee retrieved1 = employeeService.getEmployeeByUuid(uuid);
        Employee retrieved2 = employeeService.getEmployeeByUuid(uuid);

        assertSame(retrieved1, retrieved2);
    }

    @Test
    @DisplayName("Should handle special characters in employee data")
    void testCreateEmployeeWithSpecialCharacters() {
        EmployeeDTO dto = createTestEmployeeDTO("Jean-Paul", "O'Brien", 50000, 30, "Senior Engineer", "jean-paul.obrien@example.com");
        Employee created = employeeService.createEmployee(dto);

        assertEquals("Jean-Paul", created.getFirstName());
        assertEquals("O'Brien", created.getLastName());
        assertEquals("jean-paul.obrien@example.com", created.getEmail());
    }

    /**
     * helper to create a dummy EmployeeDTO
     */
    private EmployeeDTO createTestEmployeeDTO(String firstName, String lastName, Integer salary, Integer age, String jobTitle, String email) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setSalary(salary);
        dto.setAge(age);
        dto.setJobTitle(jobTitle);
        dto.setEmail(email);
        return dto;
    }
}
