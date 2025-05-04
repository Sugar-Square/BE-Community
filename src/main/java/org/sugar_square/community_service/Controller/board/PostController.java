package org.sugar_square.community_service.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.board.PostModifyDTO;
import org.sugar_square.community_service.dto.board.PostRegisterDTO;
import org.sugar_square.community_service.dto.board.PostResponseDTO;
import org.sugar_square.community_service.service.board.PostService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @PostMapping
  public ResponseEntity<String> writePost(@RequestBody @Valid final PostRegisterDTO registerDTO) {
    Long savedPostId = postService.register(registerDTO);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body("postId : " + savedPostId); // TODO: 페이지 리다이렉트
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
}
