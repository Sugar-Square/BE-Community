package org.sugar_square.community_service.service;

import static org.sugar_square.community_service.TestDataInitializer.CATEGORY_NAME;
import static org.sugar_square.community_service.TestDataInitializer.MEMBER_NICKNAME;
import static org.sugar_square.community_service.TestDataInitializer.POST_TITLE;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostModifyDTO;
import org.sugar_square.community_service.dto.board.PostRegisterDTO;
import org.sugar_square.community_service.repository.board.CategoryRepository;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.repository.member.MemberRepository;
import org.sugar_square.community_service.service.board.PostService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostServiceTest {

  @Autowired
  MemberRepository memberRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  PostService postService;
  @Autowired
  private PostRepository postRepository;

  @BeforeAll
  static void setup(@Autowired TestDataInitializer initializer) {
    initializer.init();
  }

  @AfterAll
  static void cleanup(@Autowired TestDataInitializer initializer) {
    initializer.clear();
  }

  @Test
  @DisplayName("게시물 저장 테스트")
  void registerTest() {
    List<Member> members = memberRepository.findByNickname(MEMBER_NICKNAME + "0");
    List<Category> categories = categoryRepository.findByName(CATEGORY_NAME + "0");
    PostRegisterDTO registerDTO = new PostRegisterDTO("test title", "test content",
        members.getFirst().getId(),
        categories.getFirst().getId());
    Long registeredId = postService.register(registerDTO);
    Assertions.assertThat(registeredId).isNotNull();
  }

  @Test
  @DisplayName("게시물 수정 테스트")
  void modifyTest() {
    List<Category> categories = categoryRepository.findByName(CATEGORY_NAME + "0");
    List<Post> posts = postRepository.findByTitle(POST_TITLE + "0");
    Long categoryId = categories.getFirst().getId();
    Long postId = posts.getFirst().getId();
    PostModifyDTO modifyDTO = new PostModifyDTO("modified title", "modified content", categoryId);
    postService.modify(postId, modifyDTO);
    Post modifiedPost = postService.findOneById(postId);
    Assertions.assertThat(modifiedPost)
        .extracting("title", "content", "category.id") // 객체 속성 추출
        .containsExactly("modified title", "modified content", categoryId); // 모든 값, 순서 검증
  }
}
