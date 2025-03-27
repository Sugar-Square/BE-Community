package org.sugar_square.community_service.domain.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.domain.member.Member;

@Entity
@Getter
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id", nullable = false, updatable = false)
  private Long id;

  @Column(columnDefinition = "TEXT") // postgresql TEXT
  private String content = "";

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false, updatable = false)
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  private List<Comment> children = new ArrayList<>();

  /*
   * TODO: 댓글 추가/삭제/수정, cascade -> x
   *  1. addChild
   *  2. removeChild (soft delete: "삭제된 댓글입니다" 표시)
   *  3. updateContent
   * */
}
