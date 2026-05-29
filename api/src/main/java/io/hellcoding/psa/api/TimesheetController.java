package io.hellcoding.psa.api;

import static io.hellcoding.psa.api.dto.DtoMapper.toWeeklyTimesheet;

import io.hellcoding.psa.api.dto.Dtos.ReviewTimesheetRequest;
import io.hellcoding.psa.api.dto.Dtos.SubmitTimesheetRequest;
import io.hellcoding.psa.api.dto.Dtos.WeeklyTimesheetResponse;
import io.hellcoding.psa.service.TimesheetService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @PostMapping("/submit")
    public ResponseEntity<WeeklyTimesheetResponse> submit(@Valid @RequestBody SubmitTimesheetRequest request) {
        WeeklyTimesheetResponse response = toWeeklyTimesheet(timesheetService.submit(request));
        return ResponseEntity.created(URI.create("/api/timesheets/" + response.id())).body(response);
    }

    @PostMapping("/{timesheetId}/approve")
    public WeeklyTimesheetResponse approve(
            @PathVariable Long timesheetId,
            @Valid @RequestBody ReviewTimesheetRequest request
    ) {
        return toWeeklyTimesheet(timesheetService.approve(timesheetId, request));
    }

    @PostMapping("/{timesheetId}/reject")
    public WeeklyTimesheetResponse reject(
            @PathVariable Long timesheetId,
            @Valid @RequestBody ReviewTimesheetRequest request
    ) {
        return toWeeklyTimesheet(timesheetService.reject(timesheetId, request));
    }

    @GetMapping("/{timesheetId}")
    public WeeklyTimesheetResponse get(@PathVariable Long timesheetId) {
        return toWeeklyTimesheet(timesheetService.get(timesheetId));
    }
}
