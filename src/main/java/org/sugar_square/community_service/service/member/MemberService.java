package org.sugar_square.community_service.service.member;

import static org.sugar_square.community_service.enums.RoleEnum.USER;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public Member register(final SignUpRequestDTO signUpRequestDTO) {
    // username, nickname 중복 체크
    checkDuplication(signUpRequestDTO.username(), signUpRequestDTO.nickname());
    // password 암호화
    Member newMember = org.sugar_square.community_service.domain.member.Member.builder()
        // not null
        .username(signUpRequestDTO.username())
        .password(passwordEncoder.encode(signUpRequestDTO.password())) // 비밀번호 암호화
        .nickname(signUpRequestDTO.nickname())
        .role(USER)
        // nullable
        .name(signUpRequestDTO.name())
        .birthday(signUpRequestDTO.getLocalDateBirthday())
        .email(signUpRequestDTO.email())
        .build();
    return memberRepository.save(newMember);
  }

  @Transactional
  public void remove(final Long memberId) {
    if (!memberRepository.existsById(memberId)) {
      throw new EntityNotFoundException("Member not found : " + memberId);
    }
    memberRepository.softDeleteById(memberId);
  }

  public Member findOneById(final Long memberId) {
    return memberRepository
        .findById(memberId)
        .orElseThrow(() -> new EntityNotFoundException("member not found: " + memberId));
  }

  /**
   * username, nickname 중복 체크. unique 제약조건을 걸어서 postgresql 에서 자동으로 해당 컬럼의 인덱스를 만들어줌.
   */
  public void checkDuplication(final String username, final String nickname) {
    if (memberRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Duplication check failed: username already exists");
    }
    if (memberRepository.existsByNickname(nickname)) {
      throw new IllegalArgumentException("Duplication check failed: nickname already exists");
    }
  }
}