package org.sugar_square.community_service.enums;

// PostController 에서 사용되는 정렬 속성 정의
public enum PostOrderProps {
  CREATED_AT, TITLE, WRITER, ID, INVALID;

  public static PostOrderProps fromString(final String type) {
    return switch (type) {
      case "createdAt" -> CREATED_AT;
      case "title" -> TITLE;
      case "writer" -> WRITER; // 작성자 닉네임으로 정렬 (member_id x)
      case "id" -> ID;
      default -> INVALID;
    };
  }
}
