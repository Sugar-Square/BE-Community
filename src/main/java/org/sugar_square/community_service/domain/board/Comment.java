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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.domain.member.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id", nullable = false, updatable = false)
  private Long id;

  @Column(columnDefinition = "TEXT") // postgresql TEXT
  private String content = "";

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false, updatable = false)
  private Member member; // TODO: DTO 에서 한 번 더 null 검증

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false, updatable = false)
  private Post post; // TODO: DTO 에서 한 번 더 null 검증

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parent;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  private List<Comment> children = new ArrayList<>();

  @Builder
  private Comment(String content, Member member, Post post, Comment parent) {
    this.content = content;
    this.member = member;
    this.post = post;
    if (parent != null) {
      parent.addChild(this);
    }
  }

  private void setParent(Comment parent) {
    if (parent != null) {
      this.parent = parent;
    }
  }

  private void addChild(Comment child) {
    if (child != null) {
      this.children.add(child);
      child.setParent(this);
    }
  }
}
