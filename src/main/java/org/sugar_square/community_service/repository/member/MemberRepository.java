package org.sugar_square.community_service.repository.member;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface MemberRepository extends BaseRepository<Member, Long> {

  List<Member> findByNickname(String nickname);
}
