package org.sugar_square.community_service.service;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.sugar_square.community_service.controller.board.BookmarkController.BookmarkResult;
import org.sugar_square.community_service.domain.board.BookmarkPK;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.board.BookmarkRepository;
import org.sugar_square.community_service.service.board.BookmarkService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BookmarkServiceTest {

  @Autowired
  private BookmarkService bookmarkService;

  @Autowired
  private BookmarkRepository bookmarkRepository;

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
  @DisplayName("북마크 등록 테스트")
  void registerBookmarkTest() {
    //given
    List<Member> members = testData.getMembers();
    List<Post> posts = testData.getPosts();
    Long memberId = members.getFirst().getId();
    Long postId = posts.getFirst().getId();
    //when
    BookmarkResult result = bookmarkService.register(memberId, postId);
    //then
    assertThat(result)
        .extracting("memberId", "postId")
        .containsExactly(memberId, postId);
  }

  @Test
  @DisplayName("북마크 삭제 테스트")
  void removeBookmarkTest() {
    //given
    List<Member> members = testData.getMembers();
    List<Post> posts = testData.getPosts();
    Long memberId = members.getFirst().getId();
    Long postId = posts.getFirst().getId();
    bookmarkService.register(memberId, postId); // 새로운 북마크 등록
    //when
    bookmarkService.remove(memberId, postId); // 등록한 북마크 삭제
    //then
    assertThat(
        bookmarkRepository.existsById(new BookmarkPK(posts.getFirst(), members.getFirst()))
    ).isFalse();
  }
}
