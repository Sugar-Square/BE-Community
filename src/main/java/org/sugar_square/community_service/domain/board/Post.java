package org.sugar_square.community_service.domain.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id", nullable = false, updatable = false)
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")  // postgresql TEXT
  private String content = "";

  private Long viewCount = 0L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  private Member writer; // TODO: DTO 에서 한 번 더 null 검증

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category; // TODO: DTO 에서 한 번 더 null 검증

  // TODO: 추후 이미지 관련 엔티티 및 관계 추가 고민

  @Builder
  private Post(String title, String content, Member writer, Category category) {
    this.title = title;
    this.content = content;
    this.writer = writer;
    this.category = category;
  }

  public void increaseViewCount() {
    this.viewCount++;
  }
}
