package org.sugar_square.community_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private Instant createdAt;

  @LastModifiedDate
  private Instant updatedAt;

  // TODO: 커스텀 repository 에 soft delete 메서드 정의 > 엔티티 repository 에 상속 후 사용
  private Instant deletedAt;
}
