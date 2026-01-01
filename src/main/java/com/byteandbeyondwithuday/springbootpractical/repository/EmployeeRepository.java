package com.byteandbeyondwithuday.springbootpractical.repository;

import com.byteandbeyondwithuday.springbootpractical.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    Optional<Employee> findByEmail(String email);
}
