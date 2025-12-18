package com.byteandbeyondwithuday.springbootpractical.repository;

import com.byteandbeyondwithuday.springbootpractical.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
