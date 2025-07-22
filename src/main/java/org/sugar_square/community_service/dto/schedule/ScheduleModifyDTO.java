package org.sugar_square.community_service.dto.schedule;

import jakarta.validation.constraints.NotNull;

public record ScheduleModifyDTO(
    @NotNull String title,
    @NotNull String scheduleDate, // 일정 날짜
    String notificationDate, // 알림 날짜, 사용자가 입력하지 않으면 알람 x
    String content // 일정 설명 (description), 500자 제한
) {

}
