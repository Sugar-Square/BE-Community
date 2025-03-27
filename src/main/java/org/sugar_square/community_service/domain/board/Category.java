package org.sugar_square.community_service.domain.board;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;

@Entity
@Getter
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id", nullable = false, updatable = false)
  private Long id;

  private String name;

  private String description;

  @OneToMany(mappedBy = "AnchoredPostPK.category", fetch = FetchType.LAZY)
  private List<AnchoredPost> anchoredPosts = new ArrayList<>();

  /*
   * TODO:
   *  1. 카테고리 생성/삭제 메서드
   *  2. 카테고리 이름, 설명 수정 메서드
   * */
}
