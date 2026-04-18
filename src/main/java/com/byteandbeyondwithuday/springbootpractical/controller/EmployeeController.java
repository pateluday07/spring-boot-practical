package com.byteandbeyondwithuday.springbootpractical.controller;

import com.byteandbeyondwithuday.springbootpractical.dto.EmployeeDTO;
import com.byteandbeyondwithuday.springbootpractical.service.EmployeeService;
import jakarta.validation.Valid;
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
    public ResponseEntity<HttpStatus> save(@Valid @RequestBody EmployeeDTO employeeDTO) {
        employeeService.save(employeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/transactional-demo")
    public ResponseEntity<String> runTransactionalDemo(@RequestParam(defaultValue = "true") Boolean shouldFail) {
        try {
            employeeService.createEmployeeAndIdCardForAtomicDemo(shouldFail);
            return ResponseEntity.ok("SUCCESS: Employee and IdCard saved in one transaction.");
        } catch (RuntimeException ex) {
            String message = "ROLLBACK: Transaction failed and was rolled back. Error: " + ex.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping
    public ResponseEntity<EmployeeDTO> update(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.update(employeeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/id-card")
    public ResponseEntity<HttpStatus> deleteIdCardByEmployeeId(@PathVariable Long id) {
        employeeService.deleteIdCardByEmployeeId(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<Iterable<EmployeeDTO>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

}
