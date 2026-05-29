package io.hellcoding.psa.config;

import io.hellcoding.psa.domain.BizOpty;
import io.hellcoding.psa.domain.DealStatus;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.ProjectMember;
import io.hellcoding.psa.domain.ProjectTask;
import io.hellcoding.psa.repository.BizOptyRepository;
import io.hellcoding.psa.repository.EmployeeRepository;
import io.hellcoding.psa.repository.ProjectMemberRepository;
import io.hellcoding.psa.repository.ProjectRepository;
import io.hellcoding.psa.repository.ProjectTaskRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DemoDataConfig {

    @Bean
    CommandLineRunner seedDemoData(
            EmployeeRepository employeeRepository,
            BizOptyRepository bizOptyRepository,
            ProjectRepository projectRepository,
            ProjectMemberRepository projectMemberRepository,
            ProjectTaskRepository projectTaskRepository
    ) {
        return args -> {
            if (employeeRepository.count() > 0) {
                return;
            }

            Employee pm = employeeRepository.save(new Employee("Demo PM", "pm@example.com", new BigDecimal("120.00")));
            Employee consultant = employeeRepository.save(new Employee("Demo Consultant", "consultant@example.com", new BigDecimal("85.00")));
            BizOpty bizOpty = bizOptyRepository.save(new BizOpty(
                    "PSA Modernization PoC",
                    "Demo Client",
                    DealStatus.PROPOSAL,
                    new BigDecimal("100000.00")
            ));

            LocalDate today = LocalDate.now();
            Project project = projectRepository.save(new Project(
                    bizOpty,
                    "PSA-POC",
                    "PSA Delivery API",
                    today.minusDays(30),
                    today.plusDays(90),
                    new BigDecimal("60000.00"),
                    new BigDecimal("320.00"),
                    pm
            ));

            projectMemberRepository.save(new ProjectMember(
                    project,
                    consultant,
                    "Backend Developer",
                    project.getStartDate(),
                    project.getEndDate(),
                    new BigDecimal("100.00")
            ));
            projectTaskRepository.save(new ProjectTask(
                    project,
                    "Timesheet Workflow",
                    "Submit, approve, reject workflow implementation",
                    new BigDecimal("80.00"),
                    1
            ));
        };
    }
}
