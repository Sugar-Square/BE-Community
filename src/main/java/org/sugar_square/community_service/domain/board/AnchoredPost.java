package org.sugar_square.community_service.domain.board;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sugar_square.community_service.domain.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnchoredPost extends BaseEntity {

  @EmbeddedId
  private AnchoredPostPK anchoredPostPK;  // post_id, category_id 복합키
}
