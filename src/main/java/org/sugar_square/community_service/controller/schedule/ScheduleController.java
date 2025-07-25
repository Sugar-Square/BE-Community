package org.sugar_square.community_service.controller.schedule;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.dto.schedule.ScheduleModifyDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleRegisterDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleResponseDTO;
import org.sugar_square.community_service.service.schedule.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

  // 월 단위 일정 조회 (ex: 2025-01, 2025-02 ...)
  @GetMapping
  public ResponseEntity<List<ScheduleResponseDTO>> readSchedulesForMonth(
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") final String yearMonth
  ) {
    List<ScheduleResponseDTO> schedules = scheduleService.readForMonth(yearMonth);
    return ResponseEntity.ok(schedules);
  }

  @PostMapping
  public ResponseEntity<String> registerSchedule(
      @RequestBody @Valid final ScheduleRegisterDTO registerDTO
  ) {
    Schedule savedSchedule = scheduleService.register(registerDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("Schedule id : " + savedSchedule.getId());
  }

  @PutMapping("/{scheduleId}")
  public ResponseEntity<String> modifySchedule(
      @PathVariable final Long scheduleId,
      @RequestBody @Valid final ScheduleModifyDTO modifyDTO
  ) {
    scheduleService.modify(scheduleId, modifyDTO);
    return ResponseEntity.ok("schedule modified successfully");
  }

  @DeleteMapping("/{scheduleId}")
  public ResponseEntity<String> removeSchedule(@PathVariable final Long scheduleId) {
    scheduleService.remove(scheduleId);
    return ResponseEntity.ok("schedule deleted successfully");
  }
}
