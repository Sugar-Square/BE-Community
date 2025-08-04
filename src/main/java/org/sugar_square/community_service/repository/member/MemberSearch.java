package org.sugar_square.community_service.repository.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sugar_square.community_service.domain.member.Member;

public interface MemberSearch {

  Page<Member> searchAll(String nickname, Pageable pageable);
}
