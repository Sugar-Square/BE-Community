package org.sugar_square.community_service.service.member;

import static org.sugar_square.community_service.enums.RoleEnum.ROLE_USER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.PageResponseDTO;
import org.sugar_square.community_service.dto.member.MemberResponseDTO;
import org.sugar_square.community_service.dto.member.SignUpRequestDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public PageResponseDTO<MemberResponseDTO> searchForAdmin(
      final String nickname,
      final Pageable pageable
  ) {
    Page<Member> members = memberRepository.searchAll(nickname, pageable);
    List<MemberResponseDTO> dtoList = members
        .getContent()
        .stream()
        .map(MemberResponseDTO::fromEntity)
        .toList();
    return PageResponseDTO.of(pageable, dtoList, (int) members.getTotalElements());
  }

  @Transactional
  public Member register(final SignUpRequestDTO signUpRequestDTO) {
    // username, nickname 중복 체크
    checkDuplication(signUpRequestDTO.username(), signUpRequestDTO.nickname());
    // password 암호화
    Member newMember = Member.builder()
        // not null
        .username(signUpRequestDTO.username())
        .password(passwordEncoder.encode(signUpRequestDTO.password())) // 비밀번호 암호화
        .nickname(signUpRequestDTO.nickname())
        .role(ROLE_USER) // 기본 role : USER
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