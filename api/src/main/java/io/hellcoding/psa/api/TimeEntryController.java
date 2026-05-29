package io.hellcoding.psa.api;

import static io.hellcoding.psa.api.dto.DtoMapper.toTimeEntry;

import io.hellcoding.psa.api.dto.Dtos.CreateTimeEntryRequest;
import io.hellcoding.psa.api.dto.Dtos.TimeEntryResponse;
import io.hellcoding.psa.api.dto.Dtos.UpdateTimeEntryRequest;
import io.hellcoding.psa.service.TimeEntryService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/time-entries")
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    public TimeEntryController(TimeEntryService timeEntryService) {
        this.timeEntryService = timeEntryService;
    }

    @PostMapping
    public ResponseEntity<TimeEntryResponse> create(@Valid @RequestBody CreateTimeEntryRequest request) {
        TimeEntryResponse response = toTimeEntry(timeEntryService.create(request));
        return ResponseEntity.created(URI.create("/api/time-entries/" + response.id())).body(response);
    }

    @PatchMapping("/{entryId}")
    public TimeEntryResponse update(@PathVariable Long entryId, @Valid @RequestBody UpdateTimeEntryRequest request) {
        return toTimeEntry(timeEntryService.update(entryId, request));
    }

    @DeleteMapping("/{entryId}")
    public ResponseEntity<Void> delete(@PathVariable Long entryId) {
        timeEntryService.delete(entryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<TimeEntryResponse> listByEmployeeWeek(
            @RequestParam Long employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStartDate
    ) {
        return timeEntryService.listByEmployeeWeek(employeeId, weekStartDate).stream()
                .map(io.hellcoding.psa.api.dto.DtoMapper::toTimeEntry)
                .toList();
    }
}
