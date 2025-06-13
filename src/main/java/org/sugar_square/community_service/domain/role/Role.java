package org.sugar_square.community_service.domain.role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.sugar_square.community_service.domain.BaseEntity;
import org.sugar_square.community_service.enums.RoleEnum;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Role extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id", nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  private RoleEnum name; // TODO: DTO 에서 한 번 더 null 검증

  public Role(RoleEnum name) {
    this.name = name;
  }
}
