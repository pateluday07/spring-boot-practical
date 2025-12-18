package com.byteandbeyondwithuday.springbootpractical.controller;

import com.byteandbeyondwithuday.springbootpractical.entity.Employee;
import com.byteandbeyondwithuday.springbootpractical.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> save(@RequestBody Employee employee) {
        employeeService.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Employee> update(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.update(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Iterable<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

}
