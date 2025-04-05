package org.sugar_square.community_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.BaseEntity;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID> extends JpaRepository<T, ID> {

  /**
   * 영속성 컨텍스트 자동 clear 이후 기존 엔티티는 준영속 상태가 된다.
   */
  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
  void softDeleteById(@Param("id") ID id);
}
