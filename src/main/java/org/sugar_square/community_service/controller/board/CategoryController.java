package org.sugar_square.community_service.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.board.CategoryRegisterDTO;
import org.sugar_square.community_service.service.board.CategoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * 관리자용 카테고리 등록 api. 일반 유저는 카테고리 등록 권한 없음. 카테고리 이름은 중복될 수 없음. description 은 선택 사항.
   */
  @PostMapping
  // @Secured("ROLE_ADMIN")
  public ResponseEntity<String> registerCategory(
      @RequestBody @Valid final CategoryRegisterDTO registerDTO
  ) {
    categoryService.register(registerDTO.name(), registerDTO.description());
    return ResponseEntity.ok("New category added successfully.");
  }

  @PostMapping("/check-duplication")
  public ResponseEntity<String> checkDuplication(
      @RequestBody final DuplicationCheckRequest request
  ) {
    if (!StringUtils.hasText(request.name)) {
      return ResponseEntity.badRequest().body("Category name must not be empty.");
    }
    categoryService.checkDuplication(request.name); // name 이 중복이면 예외 발생
    return ResponseEntity.ok("Duplication check passed.");
  }

  /* * * * * * * * * * * * * * * * * * * *
            NOT CONTROLLER METHOD
   * * * * * * * * * * * * * * * * * * * */
  public record DuplicationCheckRequest(String name) {

  }
}