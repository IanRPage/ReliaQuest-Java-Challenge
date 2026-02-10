package com.challenge.api.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EmployeeImpl Tests")
class EmployeeImplTest {

    private EmployeeImpl employee;
    private Instant hireDate;

    @BeforeEach
    void setUp() {
        hireDate = Instant.now();
        employee = new EmployeeImpl("John", "Doe", 50000, 30, "Software Engineer", "john@example.com", hireDate);
    }

    @Test
    @DisplayName("Should create employee with all fields")
    void testEmployeeCreation() {
        assertNotNull(employee);
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals(50000, employee.getSalary());
        assertEquals(30, employee.getAge());
        assertEquals("Software Engineer", employee.getJobTitle());
        assertEquals("john@example.com", employee.getEmail());
        assertEquals(hireDate, employee.getContractHireDate());
    }

    @Test
    @DisplayName("Should create full name correctly")
    void testFullName() {
        assertEquals("John Doe", employee.getFullName());
    }

    @Test
    @DisplayName("Should set and get UUID")
    void testUuid() {
        UUID uuid = UUID.randomUUID();
        employee.setUuid(uuid);
        assertEquals(uuid, employee.getUuid());
    }

    @Test
    @DisplayName("Should update first name")
    void testSetFirstName() {
        employee.setFirstName("Jane");
        assertEquals("Jane", employee.getFirstName());
    }

    @Test
    @DisplayName("Should update last name")
    void testSetLastName() {
        employee.setLastName("Smith");
        assertEquals("Smith", employee.getLastName());
    }

    @Test
    @DisplayName("Should update salary")
    void testSetSalary() {
        employee.setSalary(60000);
        assertEquals(60000, employee.getSalary());
    }

    @Test
    @DisplayName("Should update age")
    void testSetAge() {
        employee.setAge(35);
        assertEquals(35, employee.getAge());
    }

    @Test
    @DisplayName("Should update job title")
    void testSetJobTitle() {
        employee.setJobTitle("Senior Engineer");
        assertEquals("Senior Engineer", employee.getJobTitle());
    }

    @Test
    @DisplayName("Should update email")
    void testSetEmail() {
        employee.setEmail("newemail@example.com");
        assertEquals("newemail@example.com", employee.getEmail());
    }

    @Test
    @DisplayName("Should update full name")
    void testSetFullName() {
        employee.setFullName("Jane Smith");
        assertEquals("Jane Smith", employee.getFullName());
    }

    @Test
    @DisplayName("Should set and get contract hire date")
    void testContractHireDate() {
        Instant newDate = Instant.parse("2024-01-01T00:00:00Z");
        employee.setContractHireDate(newDate);
        assertEquals(newDate, employee.getContractHireDate());
    }

    @Test
    @DisplayName("Should set and get contract termination date")
    void testContractTerminationDate() {
        assertNull(employee.getContractTerminationDate());
        Instant terminationDate = Instant.now();
        employee.setContractTerminationDate(terminationDate);
        assertEquals(terminationDate, employee.getContractTerminationDate());
    }

    @Test
    @DisplayName("Should use current time as hire date when null")
    void testNullHireDateDefaultsToNow() {
        EmployeeImpl emp = new EmployeeImpl("Alice", "Johnson", 45000, 28, "QA Engineer", "alice@example.com", null);
        assertNotNull(emp.getContractHireDate());
        assertTrue(emp.getContractHireDate().isBefore(Instant.now().plusSeconds(5)));
    }

    @Test
    @DisplayName("Should throw NullPointerException when firstName is null")
    void testNullFirstNameThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new EmployeeImpl(null, "Doe", 50000, 30, "Engineer", "john@example.com", hireDate)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when lastName is null")
    void testNullLastNameThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new EmployeeImpl("John", null, 50000, 30, "Engineer", "john@example.com", hireDate)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when salary is null")
    void testNullSalaryThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new EmployeeImpl("John", "Doe", null, 30, "Engineer", "john@example.com", hireDate)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when age is null")
    void testNullAgeThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new EmployeeImpl("John", "Doe", 50000, null, "Engineer", "john@example.com", hireDate)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when jobTitle is null")
    void testNullJobTitleThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new EmployeeImpl("John", "Doe", 50000, 30, null, "john@example.com", hireDate)
        );
    }

    @Test
    @DisplayName("Should throw NullPointerException when email is null")
    void testNullEmailThrowsException() {
        assertThrows(NullPointerException.class, () ->
                new EmployeeImpl("John", "Doe", 50000, 30, "Engineer", null, hireDate)
        );
    }
}
