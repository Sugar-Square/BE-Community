package org.sugar_square.community_service.controller.board;

import static org.springframework.data.domain.Sort.Direction.DESC;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.PageResponseDTO;
import org.sugar_square.community_service.dto.board.BookmarkResponseDTO;
import org.sugar_square.community_service.service.board.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  // TODO: 전체 북마크 조회 - 5개씩 페이징 처리
  @GetMapping("/member/{memberId}")
  public ResponseEntity<PageResponseDTO<BookmarkResponseDTO>> getBookmarkList(
      @PathVariable("memberId") final Long memberId,
      @PageableDefault(page = 0, size = 5, sort = "createdAt", direction = DESC) Pageable pageable
  ) {
    return ResponseEntity.ok(bookmarkService.findAllByMemberId(memberId, pageable));
  }

  @PostMapping("/member/{memberId}/post/{postId}")
  public ResponseEntity<String> registerBookmark(
      @PathVariable final Long memberId,
      @PathVariable final Long postId
  ) {
    BookmarkResult result = bookmarkService.register(memberId, postId);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("member " + result.memberId() + " bookmarked post " + result.postId()
            + " successfully");
  }

  @DeleteMapping("/member/{memberId}/post/{postId}")
  public ResponseEntity<String> removeBookmark(
      @PathVariable final Long memberId,
      @PathVariable final Long postId
  ) {
    bookmarkService.remove(memberId, postId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body("Bookmark removed successfully.");
  }

  public record BookmarkResult(
      Long memberId, Long postId
  ) {

  }
}
