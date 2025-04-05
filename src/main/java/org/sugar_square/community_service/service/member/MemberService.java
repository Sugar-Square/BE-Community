package org.sugar_square.community_service.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  public Member findOneById(final Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("member not found: " + memberId));
  }
}
