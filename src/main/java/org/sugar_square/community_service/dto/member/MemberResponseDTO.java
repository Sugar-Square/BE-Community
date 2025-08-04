package org.sugar_square.community_service.dto.member;

import lombok.Builder;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.utils.StringDateConverter;

@Builder
public record MemberResponseDTO(
    Long memberId,
    String username, // login id
    String nickname,
    String name,
    String role, // RoleEnum -> String 변환 필요
    String birthday, // null 이면 빈 문자열
    String email
) {

  public static MemberResponseDTO fromEntity(Member entity) {
    return MemberResponseDTO.builder()
        .memberId(entity.getId())
        .username(entity.getUsername())
        .nickname(entity.getNickname())
        .name(entity.getName())
        .role(entity.getRole().toString())
        .birthday(StringDateConverter.localDateToString(entity.getBirthday())) // null 이면 빈 문자열
        .email(entity.getEmail())
        .build();
  }
}
