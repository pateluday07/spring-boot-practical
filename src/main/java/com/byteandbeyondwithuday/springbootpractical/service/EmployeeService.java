package com.byteandbeyondwithuday.springbootpractical.service;

import com.byteandbeyondwithuday.springbootpractical.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    void save(EmployeeDTO employeeDTO);

    EmployeeDTO update(EmployeeDTO employeeDTO);

    EmployeeDTO findById(Long id);

    void deleteById(Long id);

    void deleteIdCardByEmployeeId(Long id);

    List<EmployeeDTO> findAll();
}
