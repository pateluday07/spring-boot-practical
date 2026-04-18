package com.byteandbeyondwithuday.springbootpractical.service;

import com.byteandbeyondwithuday.springbootpractical.dto.EmployeeDTO;
import com.byteandbeyondwithuday.springbootpractical.entity.Employee;
import com.byteandbeyondwithuday.springbootpractical.entity.IdCard;
import com.byteandbeyondwithuday.springbootpractical.exception.BadRequestException;
import com.byteandbeyondwithuday.springbootpractical.exception.ErrorMessage;
import com.byteandbeyondwithuday.springbootpractical.exception.ResourceConflictException;
import com.byteandbeyondwithuday.springbootpractical.exception.ResourceNotFoundException;
import com.byteandbeyondwithuday.springbootpractical.mapper.EmployeeMapper;
import com.byteandbeyondwithuday.springbootpractical.repository.EmployeeRepository;
import com.byteandbeyondwithuday.springbootpractical.repository.IdCardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final IdCardRepository idCardRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, IdCardRepository idCardRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.idCardRepository = idCardRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public void save(EmployeeDTO employeeDTO) {
        validateEmployeeForCreate(employeeDTO);

        employeeRepository.save(employeeMapper.toEntity(employeeDTO));
    }

    @Override
    @Transactional
    public void createEmployeeAndIdCardForAtomicDemo(boolean shouldFail) {
        Employee employee = new Employee();
        employee.setFirstName("Harry");
        employee.setLastName("Potter");
        employee.setEmail("harry@gmail.com");
        employee.setSalary(new BigDecimal("15000.00"));

        // Step 1: save employee first
        Employee savedEmployee = employeeRepository.save(employee);

        if (shouldFail) {
            // Step 2 fails intentionally to demonstrate rollback of step 1 as well
            throw new RuntimeException("Simulated IdCard save failure after Employee save");
        }

        IdCard idCard = new IdCard();
        idCard.setCardNumber("CARD-" + System.nanoTime());
        idCard.setIssueDate(LocalDate.now());
        idCard.setExpiryDate(LocalDate.now().plusYears(5));

        idCard.setEmployee(savedEmployee);
        idCardRepository.save(idCard);
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        validateEmployeeForUpdate(employeeDTO);

        return employeeMapper.toDTO(employeeRepository.save(employeeMapper.toEntity(employeeDTO)));
    }

    @Override
    public EmployeeDTO findById(Long id) {
        return employeeMapper.toDTO(employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.EMPLOYEE_NOT_FOUND.formatMessage(id))));
    }

    @Override
    public void deleteById(Long id) {
        validateEmployeeExistsById(id);

        employeeRepository.deleteById(id);
    }

    @Override
    public void deleteIdCardByEmployeeId(Long id) {
        validateEmployeeExistsById(id);
        employeeRepository.findById(id).ifPresent(Employee::removeIdCard);
    }

    @Override
    public List<EmployeeDTO> findAll() {
        return employeeRepository
                .findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    private void validateEmployeeForCreate(EmployeeDTO employeeDTO) {
        validateEmployeeIdForCreate(employeeDTO.getEmployeeId());
        validateEmailUniquenessForCreate(employeeDTO.getEmail());
    }

    private void validateEmployeeIdForCreate(Long id) {
        if (id != null) {
            throw new BadRequestException(ErrorMessage.REMOVE_EMPLOYEE_ID_FROM_REQUEST.getMessage());
        }
    }

    private void validateEmailUniquenessForCreate(String email) {
        if (employeeRepository.existsByEmail(email)) {
            throw new ResourceConflictException(ErrorMessage.EMPLOYEE_EMAIL_ALREADY_EXISTS.formatMessage(email));
        }
    }

    private void validateEmployeeForUpdate(EmployeeDTO employeeDTO) {
        validateEmployeeIdForUpdate(employeeDTO.getEmployeeId());
        validateEmailUniquenessForUpdate(employeeDTO.getEmail(), employeeDTO.getEmployeeId());
    }

    private void validateEmployeeIdForUpdate(Long id) {
        if (id == null) {
            throw new BadRequestException(ErrorMessage.EMPLOYEE_ID_MUST_BE_PROVIDED.getMessage());
        }
    }

    private void validateEmailUniquenessForUpdate(String email, Long employeeId) {
        employeeRepository.findByEmail(email).ifPresent(existingEmployee -> {
            if (!existingEmployee.getId().equals(employeeId)) {
                throw new ResourceConflictException(ErrorMessage.EMPLOYEE_EMAIL_ALREADY_EXISTS.formatMessage(email));
            }
        });
    }

    private void validateEmployeeExistsById(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorMessage.EMPLOYEE_NOT_FOUND.formatMessage(id));
        }
    }

}
