package org.sugar_square.community_service.Controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.board.PostRequestDTO;
import org.sugar_square.community_service.service.board.PostService;

@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/board")
  public ResponseEntity<String> writePost(@RequestBody @Valid final PostRequestDTO requestDTO) {
    Long savedPostId = postService.register(
        requestDTO.title(),
        requestDTO.content(),
        requestDTO.memberId(),
        requestDTO.categoryId()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body("postId : " + savedPostId);
  }
}
