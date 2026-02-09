package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeDTO;
import com.challenge.api.model.EmployeeImpl;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private Map<UUID, Employee> employees = new ConcurrentHashMap<>();

    public EmployeeService() {
        // creates mock data so GET /all isn't empty
        EmployeeImpl e1 = new EmployeeImpl(
                "Ian",
                "Rodriguez-Page",
                125000,
                24,
                "Software Engineer",
                "irodriguezpage@reliaquest.com",
                Instant.now().minusSeconds(86400L * 365));
        e1.setUuid(UUID.randomUUID());
        employees.put(e1.getUuid(), e1);

        EmployeeImpl e2 = new EmployeeImpl("Ashton", "Page", 1000000, 2, "CEO", "apage@reliaquest.com", Instant.now());
        e2.setUuid(UUID.randomUUID());
        employees.put(e2.getUuid(), e2);
    }

    /**
     *
     * @return list of all employees, unfiltered
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    /**
     *
     * @param uuid
     * @return Employee object corresponding to query UUID
     */
    public Employee getEmployeeByUuid(UUID uuid) {
        return employees.get(uuid);
    }

    // TODO: add error exception handling
    public Employee createEmployee(EmployeeDTO params) {
        EmployeeImpl employee = new EmployeeImpl(
                params.getFirstName(),
                params.getLastName(),
                params.getSalary(),
                params.getAge(),
                params.getJobTitle(),
                params.getEmail(),
                params.getContractHireDate());
        employee.setUuid(UUID.randomUUID());
        employees.put(employee.getUuid(), employee);
        return employee;
    }
}
