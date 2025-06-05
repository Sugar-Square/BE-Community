package org.sugar_square.community_service.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
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
import org.sugar_square.community_service.domain.board.Comment;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostCommentModifyDTO;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.dto.board.PostCommentResponseDTO;
import org.sugar_square.community_service.repository.board.CommentRepository;
import org.sugar_square.community_service.service.board.PostCommentService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class PostCommentServiceTest {

  @Autowired
  private PostCommentService postCommentService;

  @Autowired
  private CommentRepository commentRepository;

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
  @DisplayName("댓글 저장 테스트")
  void registerTest() {
    //given
    List<Post> posts = testData.getPosts();
    Post post = posts.getFirst();
    Member member = post.getWriter();
    PostCommentRegisterDTO registerDTO = new PostCommentRegisterDTO("test content", null,
        member.getId());
    //when
    Long registeredId = postCommentService.register(post.getId(), registerDTO);
    //then
    assertThat(registeredId).isNotNull();
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
    assertThat(modified.getContent()).isNotEqualTo(content); // 기존 content 와 다르면 PASS
  }

  /*
   * comment 는 soft delete 된 엔티티도 조회함
   * 조회한 comment 중 deletedAt != null 인 댓글은 "삭제된 댓글입니다" 처리
   * */
  @Test
  @DisplayName("댓글 삭제 테스트")
  void removeTest() {
    //given
    List<Comment> comments = testData.getComments();
    Long removeId = comments.getFirst().getId();
    Long compareId = comments.getLast().getId();
    //when
    postCommentService.remove(removeId);
    //then
    Comment removed = postCommentService.findOneById(removeId);
    Comment compare = postCommentService.findOneById(compareId);
    assertThat(removed.isDeleted()).isTrue(); // is deleted
    assertThat(compare.isDeleted()).isFalse(); // is not deleted
  }

  @Test
  @DisplayName("게시글의 모든 댓글 조회 테스트")
  void readAllByPostIdTest() {
    //then
    Long postId = testData.getPosts().getFirst().getId();
    List<Comment> comments = testData.getComments();
    Iterator<Comment> iterator = comments.iterator();
    //given
    List<PostCommentResponseDTO> dtos = postCommentService.readAllByPostId(postId);
    //then
    dtos.forEach(dto -> {
      Comment comment = iterator.next();
      assertThat(dto)
          .extracting("id", "parentId", "postId", "content", "writerNickname")
          .containsExactly(
              comment.getId(),
              comment.getParent() == null ? null : comment.getParent().getId(),
              comment.getPost().getId(),
              comment.getContent(),
              comment.getWriter().getNickname()
          );
    });
  }
}
