package org.sugar_square.community_service.domain.schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "schedule_id", nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false)
  private Instant scheduleDate; // TODO: DTO 에서 한 번 더 null 검증

  @Column(nullable = false)
  private String title; // TODO: DTO 에서 한 번 더 null 검증

  private Instant notificationDate;

  @Column(columnDefinition = "TEXT") // postgresql TEXT
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  private Member member; // TODO: DTO 에서 한 번 더 null 검증

  @Builder
  private Schedule(
      Instant scheduleDate,
      String title,
      Instant notificationDate,
      String content,
      Member member) {
    this.scheduleDate = scheduleDate;
    this.title = title;
    this.notificationDate = notificationDate;
    this.content = content;
    this.member = member;
  }

  /*
   * TODO:
   *  1. 일정 시간 수정 메서드
   *  2. 알림 시간 수정 메서드 (일정 시간의 상대적 시간을 받아서 수정)
   *  */
}
