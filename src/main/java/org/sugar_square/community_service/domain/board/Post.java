package org.sugar_square.community_service.domain.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.domain.member.Member;

@Entity
@Getter
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id", nullable = false)
  private Long id;

  private String title;

  @Column(columnDefinition = "TEXT")  // postgresql TEXT
  private String content = "";

  @Column(nullable = false)
  private Long viewCount = 0L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;

  // TODO: 추후 이미지 관련 엔티티 및 관계 추가 고민

  /*
   * TODO:
   *  1. viewCount 증가 메서드
   *  2. 게시글 생성/삭제/수정 메서드
   *  3. 연관관계 메서드 ( Member, Category, Comment 생성 / 삭제 )
   *  */
}
