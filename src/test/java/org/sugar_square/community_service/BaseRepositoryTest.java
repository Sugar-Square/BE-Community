package org.sugar_square.community_service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.member.MemberRepository;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BaseRepositoryTest {

  @Autowired
  MemberRepository memberRepository;

  private Member member;

  @BeforeEach
  void setUp() {
    member = memberRepository.save(
        Member.builder()
            .username("test")
            .password("test")
            .nickname("test")
            .build()
    );
  }

  @Test
  @DisplayName("save 테스트")
  void saveTest() {
    Optional<Member> result = memberRepository.findById(member.getId());
    assertThat(result).isNotEmpty();
  }

  @Test
  @DisplayName("soft delete 조회 테스트")
  void softDeleteTest() {
    memberRepository.softDeleteById(member.getId());
    Optional<Member> result = memberRepository.findById(member.getId());
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("delete 테스트")
  void deleteTest() {
    memberRepository.delete(member);
    Optional<Member> result = memberRepository.findById(member.getId());
    assertThat(result).isEmpty();
  }
}
