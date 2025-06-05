package org.sugar_square.community_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sugar_square.community_service.TestDataInitializer.POST_CONTENT;
import static org.sugar_square.community_service.TestDataInitializer.POST_TITLE;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.TestData;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostModifyDTO;
import org.sugar_square.community_service.dto.board.PostRegisterDTO;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.service.board.PostService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostServiceTest {

  @Autowired
  private PostService postService;

  @Autowired
  private PostRepository postRepository;

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
  @DisplayName("게시물 저장 테스트")
  void registerTest() {
    //given
    List<Member> members = testData.getMembers();
    List<Category> categories = testData.getCategories();
    PostRegisterDTO registerDTO = new PostRegisterDTO(POST_TITLE, POST_CONTENT,
        members.getFirst().getId(),
        categories.getFirst().getId());
    //when
    Long registeredId = postService.register(registerDTO);
    //then
    assertThat(registeredId).isNotNull();
  }

  @Test
  @DisplayName("게시물 수정 테스트")
  void modifyTest() {
    //given
    List<Category> categories = testData.getCategories();
    List<Post> posts = testData.getPosts();
    Long categoryId = categories.getFirst().getId();
    Long postId = posts.getFirst().getId();
    PostModifyDTO modifyDTO = new PostModifyDTO("modified title", "modified content", categoryId);
    //when
    postService.modify(postId, modifyDTO);
    //then
    Post modifiedPost = postService.findOneById(postId);
    assertThat(modifiedPost)
        .extracting("title", "content", "category.id") // 객체 속성 추출
        .containsExactly("modified title", "modified content", categoryId); // 모든 값, 순서 검증
  }

  @Test
  @DisplayName("게시물 삭제 테스트")
  void removeTest() {
    //given
    List<Post> posts = testData.getPosts();
    Long postId = posts.getFirst().getId();
    //when
    postService.remove(postId);
    //then
    Optional<Post> found = postRepository.findById(postId);
    assertThat(found).isEmpty();
  }
}
