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
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.domain.member.Member;

@Entity
@Getter
public class Schedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Instant scheduleDate;

  @Column(nullable = false)
  private String title;

  private Instant notificationDate;

  @Column(columnDefinition = "TEXT") // postgresql TEXT
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  /*
   * TODO:
   *  1. 일정 생성/삭제/수정 메서드
   *  2. 일정 시간 수정 메서드
   *  3. 알림 시간 수정 메서드 (일정 시간의 상대적 시간을 받아서 수정)
   *  */
}
