package org.sugar_square.community_service.service;

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
import org.sugar_square.community_service.TestData;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.board.Comment;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostCommentModifyDTO;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.service.board.PostCommentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostCommentServiceTest {

  @Autowired
  private PostCommentService postCommentService;

  @Autowired
  private PostRepository postRepository;

  private static TestData testData;

  @BeforeAll
  static void setup(@Autowired TestDataInitializer initializer) {
    initializer.init();
    testData = new TestData(initializer);
  }

  @AfterAll
  static void cleanup(@Autowired TestDataInitializer initializer) {
    initializer.clear();
  }

  @Test
  @DisplayName("댓글 저장 테스트")
  void registerTest() {
    //given
    List<Post> posts = postRepository.findByTitle(POST_TITLE + "0");
    Post post = posts.getFirst();
    Member member = post.getWriter();
    PostCommentRegisterDTO registerDTO = new PostCommentRegisterDTO("test content", null,
        member.getId());
    //when
    Long registeredId = postCommentService.register(post.getId(), registerDTO);
    //then
    Assertions.assertThat(registeredId).isNotNull();
  }

  @Test
  @DisplayName("댓글 수정 테스트")
  void modifyTest() {
    //given
    List<Comment> comments = testData.getComments();
    Comment comment = comments.getFirst();
    String content = comment.getContent();
    PostCommentModifyDTO modifyDTO = new PostCommentModifyDTO("modified" + content);
    //when
    postCommentService.modify(comment.getId(), modifyDTO);
    //then
    Comment modified = postCommentService.findOneById(comment.getId());
    Assertions.assertThat(modified.getContent()).isNotEqualTo(content); // 기존 content 와 다르면 PASS
  }
}
