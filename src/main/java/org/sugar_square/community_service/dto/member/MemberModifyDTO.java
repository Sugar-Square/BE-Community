package org.sugar_square.community_service.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

public record MemberModifyDTO(
    @NotNull
    String nickname, // TODO : 닉네임 글자수 제한 고려
    String name,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String birthday,
    @Email(message = "You must input a valid email address")
    String email
) {
  
}
