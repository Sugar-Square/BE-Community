package org.sugar_square.community_service.service.schedule;

import static org.sugar_square.community_service.utils.StringDateConverter.stringToInstant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.dto.schedule.ScheduleModifyDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleRegisterDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.schedule.ScheduleRepository;
import org.sugar_square.community_service.service.member.MemberService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

  private final ScheduleRepository scheduleRepository;
  private final MemberService memberService;

  @Transactional
  public Schedule register(final ScheduleRegisterDTO registerDTO) {
    Member writer = memberService.findOneById(registerDTO.memberId());
    Schedule schedule = registerDTO.toEntity(writer);
    return scheduleRepository.save(schedule);
  }

  @Transactional
  public void modify(final Long scheduleId, final ScheduleModifyDTO modifyDTO) {
    Schedule schedule = findOneById(scheduleId);
    schedule.update(modifyDTO.title(),
        stringToInstant(modifyDTO.scheduleDate()),
        stringToInstant(modifyDTO.notificationDate()),
        modifyDTO.content());
  }

  public Schedule findOneById(final Long scheduleId) {
    return scheduleRepository
        .findById(scheduleId)
        .orElseThrow(() -> new EntityNotFoundException("Schedule not found : " + scheduleId));
  }
}