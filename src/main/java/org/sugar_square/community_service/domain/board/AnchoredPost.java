package org.sugar_square.community_service.domain.board;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;

@Entity
@Getter
public class AnchoredPost extends BaseEntity {

  @EmbeddedId
  private AnchoredPostPK AnchoredPostPK;  // post_id, category_id 복합키
}
