package com.challenge.api.service;

import com.challenge.api.model.Employee;
import com.challenge.api.model.EmployeeDTO;
import com.challenge.api.model.EmployeeImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private Map<UUID, Employee> employees = new ConcurrentHashMap<>();

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
