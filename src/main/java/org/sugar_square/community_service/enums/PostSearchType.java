package org.sugar_square.community_service.enums;

public enum PostSearchType {
  TITLE, CONTENT, TITLE_AND_CONTENT, WRITER, INVALID;

  public static PostSearchType fromString(final String type) {
    return switch (type) {
      case "t" -> TITLE;
      case "c" -> CONTENT;
      case "t+c" -> TITLE_AND_CONTENT;
      case "w" -> WRITER;
      default -> INVALID;
    };
  }
}
