package org.sugar_square.community_service.domain.board;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;

@Entity
@Getter
public class Bookmark extends BaseEntity {

  @EmbeddedId
  private BookmarkPK bookmarkPK; // member_id, post_id 복합키
}
