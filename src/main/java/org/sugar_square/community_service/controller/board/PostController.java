package org.sugar_square.community_service.controller.board;

import static org.springframework.data.domain.Sort.Direction.DESC;

import jakarta.validation.Valid;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.PageResponseDTO;
import org.sugar_square.community_service.dto.board.PostModifyDTO;
import org.sugar_square.community_service.dto.board.PostPreviewDTO;
import org.sugar_square.community_service.dto.board.PostRegisterDTO;
import org.sugar_square.community_service.dto.board.PostResponseDTO;
import org.sugar_square.community_service.enums.PostSearchType;
import org.sugar_square.community_service.service.board.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  
  @GetMapping("/category/{categoryId}")
  public ResponseEntity<PageResponseDTO<PostPreviewDTO>> searchCategoryPost(
      @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = DESC) final Pageable pageable,
      @PathVariable final Long categoryId,
      @RequestParam(defaultValue = "") final String searchType, // 검색 타입 (제목, 내용, 작성자, 제목+내용 등)
      @RequestParam(defaultValue = "") final String keyword,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final String startDate,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") final String endDate
  ) {
    SearchCondition condition = new SearchCondition(searchType, keyword, startDate, endDate);
    PageResponseDTO<PostPreviewDTO> result = postService.searchInCategoryPost(pageable, categoryId,
        condition);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<String> writePost(@RequestBody @Valid final PostRegisterDTO registerDTO) {
    Long savedPostId = postService.register(registerDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("postId : " + savedPostId);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostResponseDTO> readPost(@PathVariable final Long postId) {
    PostResponseDTO responseDto = postService.readOneById(postId);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @PutMapping("/{postId}")
  public ResponseEntity<String> modifyPost(
      @PathVariable final Long postId,
      @RequestBody @Valid final PostModifyDTO modifyDTO
  ) {
    postService.modify(postId, modifyDTO);
    return ResponseEntity.status(HttpStatus.OK).body("post modified successfully");
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<String> removePost(@PathVariable final Long postId) {
    postService.remove(postId);
    return ResponseEntity.status(HttpStatus.OK).body("post removed successfully");
  }

  @Getter
  @ToString
  public static class SearchCondition {

    private final PostSearchType searchType;
    private final String keyword;
    private final Instant startDate;
    private final Instant endDate;

    public SearchCondition(String searchType, String keyword, String startDate, String endDate) {
      this.searchType = PostSearchType.fromString(searchType);
      this.keyword = keyword;
      this.startDate =
          StringUtils.hasText(startDate) ? Instant.parse(startDate + "T00:00:00Z") : null;
      this.endDate =
          StringUtils.hasText(endDate) ? Instant.parse(endDate + "T00:00:00Z") : null;
    }
  }
}
