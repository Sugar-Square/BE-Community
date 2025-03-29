package org.sugar_square.community_service.repository.role;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.role.UserRole;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, Long> {

}
