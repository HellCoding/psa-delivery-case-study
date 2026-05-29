package io.hellcoding.psa.api;

import static io.hellcoding.psa.api.dto.DtoMapper.toEmployee;

import io.hellcoding.psa.api.dto.Dtos.CreateEmployeeRequest;
import io.hellcoding.psa.api.dto.Dtos.EmployeeResponse;
import io.hellcoding.psa.service.EmployeeService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeResponse response = toEmployee(employeeService.create(request));
        return ResponseEntity.created(URI.create("/api/employees/" + response.id())).body(response);
    }

    @GetMapping
    public List<EmployeeResponse> list() {
        return employeeService.list().stream().map(io.hellcoding.psa.api.dto.DtoMapper::toEmployee).toList();
    }
}
