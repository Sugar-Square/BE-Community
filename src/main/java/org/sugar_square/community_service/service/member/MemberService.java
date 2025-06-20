package org.sugar_square.community_service.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.member.SignUpRequestDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public Member register(final SignUpRequestDTO signUpRequestDTO) {
    checkDuplication(signUpRequestDTO.username(), signUpRequestDTO.nickname());
    Member newMember = Member.builder()
        .username(signUpRequestDTO.username()) // TODO: username 과 nickname 인덱스 생성 고민
        .password(signUpRequestDTO.password()) // TODO: 비밀번호 암호화 처리 필요
        .nickname(signUpRequestDTO.nickname())
        .name(signUpRequestDTO.name())
        .birthday(signUpRequestDTO.getLocalDateBirthday())
        .email(signUpRequestDTO.email())
        .build();
    return memberRepository.save(newMember);
  }

  public Member findOneById(final Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("member not found: " + memberId));
  }

  public void checkDuplication(final String username, final String nickname) {
    if (memberRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Duplication check failed: username already exists");
    }
    if (memberRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("Duplication check failed: nickname already exists");
    }
  }
}
