package org.sugar_square.community_service.controller.schedule;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.dto.schedule.ScheduleModifyDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleRegisterDTO;
import org.sugar_square.community_service.service.schedule.ScheduleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

  private final ScheduleService scheduleService;

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
}
