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
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.repository.member.MemberRepository;
import org.sugar_square.community_service.service.board.PostCommentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostCommentServiceTest {

  @Autowired
  private PostCommentService postCommentService;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private MemberRepository memberRepository;

  @BeforeAll
  static void setup(@Autowired TestDataInitializer initializer) {
    initializer.init();
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
}
