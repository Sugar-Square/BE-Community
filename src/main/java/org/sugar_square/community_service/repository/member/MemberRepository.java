package org.sugar_square.community_service.repository.member;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long> {

  boolean existsByUsername(String username);

  boolean existsByNickname(String nickname);
}
