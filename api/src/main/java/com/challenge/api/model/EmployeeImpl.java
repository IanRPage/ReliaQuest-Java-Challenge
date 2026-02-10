package com.challenge.api.model;

import java.time.Instant;
import java.util.UUID;
import lombok.NonNull;

public class EmployeeImpl implements Employee {
    private UUID uuid;

    @NonNull private String firstName;

    @NonNull private String lastName;

    private String fullName;

    @NonNull private Integer salary;

    @NonNull private Integer age;

    @NonNull private String jobTitle;

    @NonNull private String email;

    private Instant contractHireDate;
    private Instant contractTerminationDate;

    public EmployeeImpl(
            @NonNull String firstName,
            @NonNull String lastName,
            @NonNull Integer salary,
            @NonNull Integer age,
            @NonNull String jobTitle,
            @NonNull String email,
            Instant contractHireDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = firstName + " " + lastName;
        this.salary = salary;
        this.age = age;
        this.jobTitle = jobTitle;
        this.email = email;

        // assume contract hire is now if hire date isn't specified
        this.contractHireDate = contractHireDate != null ? contractHireDate : Instant.now();

        this.contractTerminationDate = null;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public Integer getSalary() {
        return salary;
    }

    @Override
    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String getJobTitle() {
        return jobTitle;
    }

    @Override
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Instant getContractHireDate() {
        return contractHireDate;
    }

    @Override
    public void setContractHireDate(Instant date) {
        this.contractHireDate = date;
    }

    @Override
    public Instant getContractTerminationDate() {
        return contractTerminationDate;
    }

    @Override
    public void setContractTerminationDate(Instant date) {
        this.contractTerminationDate = date;
    }
}
