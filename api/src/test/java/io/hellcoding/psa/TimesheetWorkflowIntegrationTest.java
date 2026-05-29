package io.hellcoding.psa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hellcoding.psa.domain.BizOpty;
import io.hellcoding.psa.domain.DealStatus;
import io.hellcoding.psa.domain.Employee;
import io.hellcoding.psa.domain.Project;
import io.hellcoding.psa.domain.ProjectMember;
import io.hellcoding.psa.domain.ProjectTask;
import io.hellcoding.psa.domain.TimeEntry;
import io.hellcoding.psa.domain.TimeEntryStatus;
import io.hellcoding.psa.repository.BizOptyRepository;
import io.hellcoding.psa.repository.EmployeeRepository;
import io.hellcoding.psa.repository.ProjectMemberRepository;
import io.hellcoding.psa.repository.ProjectRepository;
import io.hellcoding.psa.repository.ProjectTaskRepository;
import io.hellcoding.psa.repository.TimeEntryRepository;
import io.hellcoding.psa.repository.WeeklyTimesheetRepository;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TimesheetWorkflowIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    BizOptyRepository bizOptyRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    ProjectTaskRepository projectTaskRepository;

    @Autowired
    TimeEntryRepository timeEntryRepository;

    @Autowired
    WeeklyTimesheetRepository weeklyTimesheetRepository;

    Employee projectManager;
    Employee consultant;
    Project project;
    ProjectTask task;
    LocalDate weekStart;
    LocalDate entryDate;

    @BeforeEach
    void setUp() {
        timeEntryRepository.deleteAll();
        weeklyTimesheetRepository.deleteAll();
        projectTaskRepository.deleteAll();
        projectMemberRepository.deleteAll();
        projectRepository.deleteAll();
        bizOptyRepository.deleteAll();
        employeeRepository.deleteAll();

        weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        entryDate = weekStart.plusDays(1);

        projectManager = employeeRepository.save(new Employee("PM", "pm@example.com", new BigDecimal("120.00")));
        consultant = employeeRepository.save(new Employee("Consultant", "consultant@example.com", new BigDecimal("85.00")));
        BizOpty bizOpty = bizOptyRepository.save(new BizOpty(
                "PSA PoC",
                "Demo Client",
                DealStatus.PROPOSAL,
                new BigDecimal("100000.00")
        ));
        project = projectRepository.save(new Project(
                bizOpty,
                "PSA-TEST",
                "PSA Test Project",
                weekStart.minusDays(7),
                weekStart.plusDays(30),
                new BigDecimal("60000.00"),
                new BigDecimal("320.00"),
                projectManager
        ));
        projectMemberRepository.save(new ProjectMember(
                project,
                consultant,
                "Developer",
                project.getStartDate(),
                project.getEndDate(),
                new BigDecimal("100.00")
        ));
        task = projectTaskRepository.save(new ProjectTask(
                project,
                "Timesheet Workflow",
                "Workflow test task",
                new BigDecimal("80.00"),
                1
        ));
    }

    @Test
    void createsTimeEntryWithCostSnapshot() throws Exception {
        long entryId = createTimeEntry(new BigDecimal("8.0"));

        TimeEntry saved = timeEntryRepository.findById(entryId).orElseThrow();
        assertThat(saved.getHourlyRateSnapshot()).isEqualByComparingTo("85.00");
        assertThat(saved.getCostAmount()).isEqualByComparingTo("680.00");

        consultant.setHourlyRate(new BigDecimal("100.00"));
        employeeRepository.save(consultant);

        TimeEntry reloaded = timeEntryRepository.findById(entryId).orElseThrow();
        assertThat(reloaded.getHourlyRateSnapshot()).isEqualByComparingTo("85.00");
        assertThat(reloaded.getCostAmount()).isEqualByComparingTo("680.00");
    }

    @Test
    void submittedTimesheetLocksTimeEntryEdits() throws Exception {
        long entryId = createTimeEntry(new BigDecimal("8.0"));

        mockMvc.perform(post("/api/timesheets/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "employeeId", consultant.getId(),
                                "weekStartDate", weekStart
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.totalHours").value(8.0));

        TimeEntry submitted = timeEntryRepository.findById(entryId).orElseThrow();
        assertThat(submitted.getStatus()).isEqualTo(TimeEntryStatus.SUBMITTED);

        mockMvc.perform(patch("/api/time-entries/{entryId}", entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("hours", new BigDecimal("6.0")))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Submitted or approved weeks cannot be edited"));
    }

    @Test
    void rejectRequiresReasonAndReopensEntriesForEdit() throws Exception {
        long entryId = createTimeEntry(new BigDecimal("8.0"));
        long timesheetId = submitTimesheet();

        mockMvc.perform(post("/api/timesheets/{timesheetId}/reject", timesheetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("reviewerId", projectManager.getId()))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Reject reason is required"));

        mockMvc.perform(post("/api/timesheets/{timesheetId}/reject", timesheetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "reviewerId", projectManager.getId(),
                                "rejectReason", "Task selection needs correction"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));

        assertThat(timeEntryRepository.findById(entryId).orElseThrow().getStatus()).isEqualTo(TimeEntryStatus.REJECTED);

        mockMvc.perform(patch("/api/time-entries/{entryId}", entryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("hours", new BigDecimal("6.0")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.hours").value(6.0));
    }

    @Test
    void approveSynchronizesEntryStatus() throws Exception {
        long entryId = createTimeEntry(new BigDecimal("8.0"));
        long timesheetId = submitTimesheet();

        mockMvc.perform(post("/api/timesheets/{timesheetId}/approve", timesheetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("reviewerId", projectManager.getId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));

        assertThat(timeEntryRepository.findById(entryId).orElseThrow().getStatus()).isEqualTo(TimeEntryStatus.APPROVED);
    }

    private long createTimeEntry(BigDecimal hours) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/time-entries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "employeeId", consultant.getId(),
                                "projectId", project.getId(),
                                "taskId", task.getId(),
                                "entryDate", entryDate,
                                "hours", hours
                        ))))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private long submitTimesheet() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/timesheets/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "employeeId", consultant.getId(),
                                "weekStartDate", weekStart
                        ))))
                .andExpect(status().isCreated())
                .andReturn();
        return readId(result);
    }

    private long readId(MvcResult result) throws Exception {
        JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
        return json.get("id").asLong();
    }
}
