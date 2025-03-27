package org.sugar_square.community_service.domain.report;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "report_id", nullable = false, updatable = false)
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Enumerated(EnumType.STRING)
  private ErrorGroup errorGroup;

  @Enumerated(EnumType.STRING)
  private ErrorCode errorCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  private Member member; // TODO: DTO 에서 한 번 더 null 검증

  @Builder
  private Report(String title, String content, ErrorGroup errorGroup, ErrorCode errorCode,
      Member member) {
    this.title = title;
    this.content = content;
    this.errorGroup = errorGroup;
    this.errorCode = errorCode;
    this.member = member;
  }
}
