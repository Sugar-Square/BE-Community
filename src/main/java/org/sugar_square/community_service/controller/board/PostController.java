package org.sugar_square.community_service.controller.board;

import static org.springframework.data.domain.Sort.Direction.DESC;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.sugar_square.community_service.service.board.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  // TODO: 카테고리 별 전체 게시글 조회 메서드, 페이징 처리
  // TODO: 검색 구현 (제목, 내용, 작성자, 제목+내용, 날짜 등)
  @GetMapping("/category/{categoryId}")
  public ResponseEntity<PageResponseDTO<PostPreviewDTO>> readPostList(
      @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = DESC) final Pageable pageable,
      @PathVariable final Long categoryId,
      @RequestParam(defaultValue = "") final String searchType, // 검색 타입 (제목, 내용, 작성자, 제목+내용 등)
      @RequestParam(defaultValue = "") final String keyword,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yy-MM-dd") final LocalDateTime startDate,
      @RequestParam(required = false) @DateTimeFormat(pattern = "yy-MM-dd") final LocalDateTime endDate
  ) {
    SearchCondition condition = new SearchCondition(keyword, searchType, startDate, endDate);
    PageResponseDTO<PostPreviewDTO> result = postService.readPostList(pageable, categoryId,
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

  public record SearchCondition(
      String searchType,
      String keyword,
      @DateTimeFormat(pattern = "yy-MM-dd") LocalDateTime startDate,
      @DateTimeFormat(pattern = "yy-MM-dd") LocalDateTime endDate
  ) {

  }
}
