package org.sugar_square.community_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.sugar_square.community_service.TestDataInitializer.POST_CONTENT;
import static org.sugar_square.community_service.TestDataInitializer.POST_TITLE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.TestData;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.controller.board.PostController.SearchCondition;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.PageResponseDTO;
import org.sugar_square.community_service.dto.board.PostModifyDTO;
import org.sugar_square.community_service.dto.board.PostPreviewDTO;
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

  @Test
  @DisplayName("카테고리 게시물 조건 검색 테스트")
  void searchInCategoryPostTest() {
    // given
    final int MAX_PAGE = 4;
    List<Post> posts = testData.getPosts();
    // when
    List<PageResponseDTO<PostPreviewDTO>> responses = new ArrayList<>();
    for (int i = 0; i < MAX_PAGE; i++) {
      Params params = genSearchInCategoryPostParams(
          i, 3, "t", "title", null, null
      );
      PageResponseDTO<PostPreviewDTO> responseDTO = postService.searchInCategoryPost(
          params.pageable,
          params.categoryId,
          params.searchCondition
      );
      responses.addLast(responseDTO);
    }
    // then
    // 생성된 Post list 와 응답 DTO 의 dtoList 를 비교
    for (int i = 0; i < MAX_PAGE; i++) {
      int start = i * 3;
      int end = Math.min(start + 3, 10);
      assertThat(responses.get(i)).
          extracting("dtoList")
          .usingRecursiveComparison()
          .isEqualTo(
              posts.subList(start, end).stream()
                  .map(PostPreviewDTO::fromEntity)
                  .toList()
          );
    }
  }

  /* * * * * * * * * * * * * * * * * * * *
              NOT TEST METHODS
   * * * * * * * * * * * * * * * * * * * */

  private Params genSearchInCategoryPostParams(
      int pageNumber, int pageSize, // for Pageable
      String searchType, String keyword, String startDate, String endDate // for SearchCondition
  ) {
    Long categoryId = testData.getCategories().getFirst().getId(); // category
    Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(ASC, "createdAt"));
    SearchCondition condition = new SearchCondition(searchType, keyword, startDate, endDate);
    return new Params(categoryId, pageable, condition);
  }

  private record Params(
      Long categoryId,
      Pageable pageable,
      SearchCondition searchCondition
  ) {

  }
}
