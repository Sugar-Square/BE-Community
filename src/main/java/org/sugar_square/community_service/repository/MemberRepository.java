package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.member.Member;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long>{

}
