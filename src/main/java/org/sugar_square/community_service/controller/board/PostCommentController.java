package org.sugar_square.community_service.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.board.PostCommentModifyDTO;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.service.board.PostCommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class PostCommentController {

  private final PostCommentService postCommentService;

  @PostMapping
  public ResponseEntity<String> writeComment(
      @PathVariable final Long postId,
      @RequestBody @Valid final PostCommentRegisterDTO registerDTO
  ) {
    Long savedCommentId = postCommentService.register(postId, registerDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("commentId : " + savedCommentId);
  }

  @PutMapping("/{commentId}")
  public ResponseEntity<String> modifyComment(
      @PathVariable final Long postId,
      @PathVariable final Long commentId,
      @RequestBody @Valid final PostCommentModifyDTO modifyDTO
  ) {
    postCommentService.modify(commentId, modifyDTO);
    return ResponseEntity.status(HttpStatus.OK).body("comment modified successfully");
  }
}
