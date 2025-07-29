package org.sugar_square.community_service.dto.schedule;

import java.time.Instant;
import lombok.Builder;
import org.sugar_square.community_service.domain.schedule.Schedule;

@Builder
public record ScheduleResponseDTO(
    Long id,
    Long memberId,
    String title,
    String content,
    Instant scheduleDate,
    Instant notificationDate,
    String writerNickname
) {

  public static ScheduleResponseDTO fromEntity(final Schedule entity) {
    return ScheduleResponseDTO.builder()
        .id(entity.getId())
        .memberId(entity.getWriter().getId())
        .title(entity.getTitle())
        .content(entity.getContent())
        .scheduleDate(entity.getScheduleDate())
        .notificationDate(entity.getNotificationDate())
        .writerNickname(entity.getWriter().getNickname())
        .build();
  }
}
