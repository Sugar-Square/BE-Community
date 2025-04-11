package org.sugar_square.community_service.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostRegisterDTO;
import org.sugar_square.community_service.repository.board.CategoryRepository;
import org.sugar_square.community_service.repository.member.MemberRepository;
import org.sugar_square.community_service.service.board.PostService;

@SpringBootTest
@Transactional
public class PostServiceTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  PostService postService;

  @Test
  @DisplayName("게시물 저장 테스트")
  void registerTest() {
    Member member = Member.builder()
        .username("test")
        .password("test")
        .nickname("test")
        .build();
    Category category = Category.builder().build();
    Member savedMember = memberRepository.save(member);
    Category savedCategory = categoryRepository.save(category);
    PostRegisterDTO registerDTO = new PostRegisterDTO("test", "test", savedMember.getId(),
        savedCategory.getId());
    Long registeredId = postService.register(registerDTO);
    Assertions.assertThat(registeredId).isNotNull();
  }
}
