package org.sugar_square.community_service.utils;

import jakarta.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

public class StringDateConverter {

  @Nullable
  public static Instant stringToInstant(final String date) {
    if (!StringUtils.hasText(date)) {
      return null;
    }
    return Instant.parse(date);
  }

  @Nullable
  public static LocalDate stringToLocalDate(final String date) {
    if (!StringUtils.hasText(date)) {
      return null;
    }
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
  }
}
