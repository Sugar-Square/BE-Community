package org.sugar_square.community_service.dto.schedule;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.util.StringUtils;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.dto.RequestDTO;

public record ScheduleRegisterDTO(
    @NotNull(message = "You must input a Schedule Title")
    String title,
    @NotNull(message = "You must input a Schedule Date")
    @DateTimeFormat(iso = ISO.DATE_TIME)
    String scheduleDate, // 일정 날짜
    @DateTimeFormat(iso = ISO.DATE_TIME)
    String notificationDate, // 알림 날짜, 사용자가 입력하지 않으면 알람 x
    @Size(max = 500, message = "Max content length is 500 characters")
    String content, // 일정 설명 (description)
    @NotNull(message = "You must input a Member Id")
    Long memberId // 작성자 ID
) implements RequestDTO<Schedule> {

  @Override
  public Schedule toEntity(final Object... args) {
    Member member = (Member) args[0];
    return Schedule.builder()
        .title(title)
        .scheduleDate(convertToInstant(scheduleDate))
        .notificationDate(convertToInstant(notificationDate))
        .content(content)
        .writer(member)
        .build();
  }

  private Instant convertToInstant(final String date) {
    if (!StringUtils.hasText(date)) {
      return null;
    }
    return Instant.parse(date);
  }
}
