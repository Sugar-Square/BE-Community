package org.sugar_square.community_service.service.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.TestData;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.dto.schedule.ScheduleModifyDTO;
import org.sugar_square.community_service.dto.schedule.ScheduleRegisterDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.utils.StringDateConverter;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ScheduleServiceTest {

  private TestData testData;
  @Autowired
  private ScheduleService scheduleService;

  @BeforeEach
  void setup(@Autowired TestDataInitializer initializer) {
    initializer.init();
    testData = new TestData(initializer);
  }

  @AfterEach
  void cleanup(@Autowired TestDataInitializer initializer) {
    initializer.clear();
  }

  @Test
  @DisplayName("일정 등록 테스트")
  void registerScheduleTest() {
    // given
    final String title = "test_schedule";
    final String scheduleDate = "2025-08-05T10:00:00Z";
    final String notificationDate = "2025-08-04T10:00:00Z";
    final String content = "this is a test schedule";
    Long memberId = testData.getMembers().getFirst().getId();
    ScheduleRegisterDTO registerDTO = new ScheduleRegisterDTO(
        title,
        scheduleDate,
        notificationDate,
        content,
        memberId
    );
    Schedule expectedSchedule = registerDTO.toEntity(testData.getMembers().getFirst());
    // when
    Schedule savedSchedule = scheduleService.register(registerDTO);
    // then
    assertThat(savedSchedule)
        .usingRecursiveComparison()
        .ignoringFields("id", "createdAt", "updatedAt", "deletedAt")
        .isEqualTo(expectedSchedule);
  }

  @Test
  @DisplayName("일정 수정 테스트")
  void modifyScheduleTest() {
    // given
    final String modifiedTitle = "modified title";
    final String modifiedScheduleDate = "2025-01-01T12:00:00Z";
    final String modifiedNotificationDate = "2024-12-31T12:00:00Z";
    final String modifiedContent = "modified content";
    Long scheduleId = testData.getSchedules().getFirst().getId(); // 첫 번째 일정을 수정
    ScheduleModifyDTO dto = new ScheduleModifyDTO(
        modifiedTitle,
        modifiedScheduleDate,
        modifiedNotificationDate,
        modifiedContent
    );
    // when
    scheduleService.modify(scheduleId, dto);
    // then
    Schedule modified = scheduleService.findOneById(scheduleId);
    assertThat(modified)
        .extracting("title", "scheduleDate", "notificationDate", "content")
        .containsExactly(
            modifiedTitle,
            StringDateConverter.stringToInstant(modifiedScheduleDate),
            StringDateConverter.stringToInstant(modifiedNotificationDate),
            modifiedContent
        );
  }

  @Test
  @DisplayName("일정 삭제 테스트")
  void removeScheduleTest() {
    // given
    Schedule schedule = testData.getSchedules().getFirst();
    // when
    scheduleService.remove(schedule.getId());
    // then
    assertThatThrownBy(() -> scheduleService.findOneById(schedule.getId()))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Schedule not found : " + schedule.getId());
  }
}
