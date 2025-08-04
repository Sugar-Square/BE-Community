package org.sugar_square.community_service.enums;

public enum MemberOrderProps {
  NICKNAME, INVALID;

  public static MemberOrderProps fromString(final String type) {
    // 추후 member 의 정렬 조건을 추가하여 확장할 수 있음
    return switch (type) {
      case "nickname" -> NICKNAME;
      default -> INVALID;
    };
  }
}
