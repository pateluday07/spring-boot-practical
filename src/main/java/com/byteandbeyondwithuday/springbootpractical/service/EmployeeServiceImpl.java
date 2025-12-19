package com.byteandbeyondwithuday.springbootpractical.service;

import com.byteandbeyondwithuday.springbootpractical.dto.EmployeeDTO;
import com.byteandbeyondwithuday.springbootpractical.mapper.EmployeeMapper;
import com.byteandbeyondwithuday.springbootpractical.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        employeeRepository.save(employeeMapper.toEntity(employeeDTO));
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        return employeeMapper.toDTO(employeeRepository.save(employeeMapper.toEntity(employeeDTO)));
    }

    @Override
    public EmployeeDTO findById(Long id) {
        return employeeMapper.toDTO(employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

}
