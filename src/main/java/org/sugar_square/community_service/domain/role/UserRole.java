package org.sugar_square.community_service.domain.role;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Getter;
import org.sugar_square.community_service.domain.BaseEntity;

@Entity
@Getter
public class UserRole extends BaseEntity {

  @EmbeddedId
  private UserRolePK userRolePK; // member_id, role_id 복합키
}
