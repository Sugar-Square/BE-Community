package org.sugar_square.community_service.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.service.board.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
public class BookmarkController {

  private final BookmarkService bookmarkService;

  // TODO: 전체 북마크 조회 - 5개씩 페이징 처리

  @PostMapping("/{memberId}/{postId}")
  public ResponseEntity<String> registerBookmark(
      @PathVariable @Valid final Long memberId,
      @PathVariable @Valid final Long postId) {
    BookmarkResult result = bookmarkService.register(memberId, postId);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body("member " + result.memberId() + " bookmarked post " + result.postId()
            + " successfully");
  }

  // TODO: 북마크 삭제

  public record BookmarkResult(
      Long memberId, Long postId
  ) {

  }
}
