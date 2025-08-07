package org.sugar_square.community_service.controller.member;

import static org.springframework.data.domain.Sort.Direction.ASC;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.sugar_square.community_service.dto.member.MemberModifyDTO;
import org.sugar_square.community_service.dto.member.MemberResponseDTO;
import org.sugar_square.community_service.dto.member.SignUpRequestDTO;
import org.sugar_square.community_service.service.member.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;

  /**
   * 관리자용 회원 목록 조회 api. pagination, sorting(닉네임), 검색 처리. notification 서비스의 유저 목록 조회 {userid, 닉네임} 와
   * 용도 다름.
   */
  @GetMapping
//  @Secured("ROLE_ADMIN")
  public ResponseEntity<PageResponseDTO<MemberResponseDTO>> searchMembersForAdmin(
      @PageableDefault(page = 0, size = 10, sort = "nickname", direction = ASC) final Pageable pageable,
      @RequestParam(required = false) final String nickname
  ) {
    PageResponseDTO<MemberResponseDTO> result = memberService.searchForAdmin(nickname, pageable);
    return ResponseEntity.ok(result);
  }

  /**
   * 일반 유저용 회원가입 api 관리자는 db query 로 직접 생성
   */
  @PostMapping("/signup")
  public ResponseEntity<String> signUp(
      @RequestBody @Valid final SignUpRequestDTO signUpRequestDTO
  ) {
    memberService.register(signUpRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body("Signed up successfully");
  }

  @PostMapping("/check-duplication")
  public ResponseEntity<String> checkDuplication(
      @RequestBody final DuplicationCheckRequest request
  ) {
    if (!StringUtils.hasText(request.username()) || !StringUtils.hasText(request.nickname())) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body("username & nickname must not be empty");
    }
    memberService.checkDuplication(request.username(), request.nickname());
    return ResponseEntity.ok("Duplication check passed");
  }

  @PutMapping("/{memberId}")
  public ResponseEntity<String> modifyMember(
      @PathVariable final Long memberId,
      @RequestBody @Valid final MemberModifyDTO modifyDTO
  ) {
    memberService.modify(memberId, modifyDTO);
    return ResponseEntity.ok("Member modified successfully");
  }

  @DeleteMapping("/{memberId}")
  public ResponseEntity<String> withdrawMember(@PathVariable final Long memberId) {
    memberService.remove(memberId);
    return ResponseEntity.ok("Member withdrawn successfully");
  }

  /* * * * * * * * * * * * * * * * * * * *
                  NOT TEST
   * * * * * * * * * * * * * * * * * * * */
  public record DuplicationCheckRequest(String username, String nickname) {

  }
}