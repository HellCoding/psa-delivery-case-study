package io.hellcoding.psa.service;

import io.hellcoding.psa.api.dto.Dtos.CreateEmployeeRequest;
import io.hellcoding.psa.api.error.DomainException;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.repository.EmployeeRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee create(CreateEmployeeRequest request) {
        employeeRepository.findByEmail(request.email()).ifPresent(employee -> {
            throw new DomainException("Employee email already exists: " + request.email());
        });
        return employeeRepository.save(new Employee(request.name(), request.email(), request.hourlyRate()));
    }

    @Transactional(readOnly = true)
    public List<Employee> list() {
        return employeeRepository.findAll();
    }
}
