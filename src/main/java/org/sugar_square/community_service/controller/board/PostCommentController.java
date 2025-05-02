package org.sugar_square.community_service.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.service.board.PostCommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class PostCommentController {

  private final PostCommentService postCommentService;

  @PostMapping
  public ResponseEntity<String> writeComment(
      @PathVariable Long postId,
      @RequestBody @Valid PostCommentRegisterDTO registerDTO
  ) {
    Long savedCommentId = postCommentService.register(postId, registerDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("commentId : " + savedCommentId);
  }
}
