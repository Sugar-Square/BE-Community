package org.sugar_square.community_service.service.schedule;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.sugar_square.community_service.dto.schedule.ScheduleRegisterDTO;

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
}
