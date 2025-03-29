package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.role.UserRole;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

}
