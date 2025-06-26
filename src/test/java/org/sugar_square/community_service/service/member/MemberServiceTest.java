package org.sugar_square.community_service.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.TestData;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.member.SignUpRequestDTO;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class MemberServiceTest {

  @Autowired
  private MemberService memberService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  private TestData testData;

  @BeforeEach
  void setup(@Autowired TestDataInitializer initializer) {
    initializer.init();
    testData = new TestData(initializer);
  }

  @AfterEach
  void cleanup(@Autowired TestDataInitializer initializer) {
    initializer.clear();
  }

  @Test
  @DisplayName("회원 가입 테스트")
  void signUpTest() {
    // given
    Member member = testData.getMembers().getFirst();
    SignUpRequestDTO dto = new SignUpRequestDTO(
        member.getUsername(),
        member.getPassword(),
        member.getNickname(),
        member.getName(),
        member.getBirthday().toString(),
        member.getEmail()
    );
    SignUpRequestDTO dto2 = new SignUpRequestDTO(
        member.getUsername() + "temp",
        member.getPassword(),
        member.getNickname() + "temp",
        member.getName(),
        member.getBirthday().toString(),
        member.getEmail()
    );
    // when
    Member saved2 = memberService.register(dto2); // success test
    // then
    // exception test
    assertThatThrownBy(() -> memberService.register(dto))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Duplication check failed: username already exists");
    // field check
    assertThat(saved2).extracting(
        Member::getUsername,
        Member::getNickname,
        Member::getBirthday,
        Member::getEmail
    ).containsExactly(
        member.getUsername() + "temp",
        member.getNickname() + "temp",
        member.getBirthday(),
        member.getEmail()
    );
    // password check
    assertThat(passwordEncoder.matches(member.getPassword(), saved2.getPassword())).isTrue();
  }

  @Test
  @DisplayName("회원 정보 중복 체크 테스트")
  void checkDuplicationTest() {
    // given
    Member member = testData.getMembers().getFirst();
    // when
    // then
    assertThatThrownBy(
        () -> memberService.checkDuplication(member.getUsername(), member.getNickname()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Duplication check failed: username already exists");
  }
}
