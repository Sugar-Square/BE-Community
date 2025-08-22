package org.sugar_square.community_service.utils;

import java.util.Objects;

public class StringUtils {

  public static String removeAllWhitespaces(String input) {
    if (Objects.isNull(input) || input.isBlank()) {
      throw new IllegalArgumentException("Input string must not be null or blank");
    }
    return input.replaceAll("\\s+", ""); // remove all whitespaces
  }
}
