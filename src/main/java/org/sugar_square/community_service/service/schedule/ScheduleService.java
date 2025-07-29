package org.sugar_square.community_service.service.schedule;

import static org.sugar_square.community_service.utils.StringDateConverter.stringToInstant;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.dto.schedule.ScheduleModifyDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleRegisterDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleResponseDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.schedule.ScheduleRepository;
import org.sugar_square.community_service.service.member.MemberService;
import org.sugar_square.community_service.utils.StringDateConverter;
import org.sugar_square.community_service.utils.StringDateConverter.InstantYearMonth;

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

  @Transactional
  public void remove(final Long scheduleId) {
    if (!scheduleRepository.existsById(scheduleId)) {
      throw new EntityNotFoundException("Schedule not found : " + scheduleId);
    }
    scheduleRepository.softDeleteById(scheduleId);
  }

  public List<ScheduleResponseDTO> readForMonth(final String yearMonth) {
    // yearMonth 가 null 이면 현재 연월로 설정 (stringYearMonthToInstantStartEnd 메서드에서 처리)
    InstantYearMonth startEnd = StringDateConverter.stringYearMonthToInstantStartEnd(yearMonth);
    return scheduleRepository.findScheduleByScheduleDateBetween(startEnd.start(), startEnd.end())
        .stream()
        .map(ScheduleResponseDTO::fromEntity)
        .toList();
  }

  public Schedule findOneById(final Long scheduleId) {
    return scheduleRepository
        .findById(scheduleId)
        .orElseThrow(() -> new EntityNotFoundException("Schedule not found : " + scheduleId));
  }
}