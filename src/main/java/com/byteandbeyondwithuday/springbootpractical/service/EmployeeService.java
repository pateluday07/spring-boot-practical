package com.byteandbeyondwithuday.springbootpractical.service;

import com.byteandbeyondwithuday.springbootpractical.entity.Employee;

import java.util.List;

public interface EmployeeService {

    void save(Employee employee);

    Employee update(Employee employee);

    Employee findById(Long id);

    void deleteById(Long id);

    List<Employee> findAll();
}
