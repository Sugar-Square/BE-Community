package org.sugar_square.community_service.controller.member;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sugar_square.community_service.dto.member.SignUpRequestDTO;
import org.sugar_square.community_service.service.member.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/signup")
  public ResponseEntity<String> signUp(
      @RequestBody @Valid final SignUpRequestDTO signUpRequestDTO
  ) {
    memberService.register(signUpRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("Signed up successfully");
  }

  // TODO: username, nickname 중복 체크 API 추가
}
