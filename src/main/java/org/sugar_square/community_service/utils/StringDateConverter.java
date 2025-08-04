package org.sugar_square.community_service.utils;

import jakarta.annotation.Nullable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
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

  public static String localDateToString(final LocalDate date) {
    if (Objects.isNull(date)) {
      return "";
    }
    return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  public static InstantYearMonth stringYearMonthToInstantStartEnd(String yearMonth) {
    YearMonth ym;
    if (!StringUtils.hasText(yearMonth)) {
      ym = YearMonth.now();
    } else {
      ym = YearMonth.parse(yearMonth);
    }
    LocalDateTime startOfMonth = ym.atDay(1).atStartOfDay();
    LocalDateTime endOfMonth = ym.atEndOfMonth().atTime(23, 59, 59);
    Instant startInstant = startOfMonth.toInstant(ZoneOffset.UTC);
    Instant endInstant = endOfMonth.toInstant(ZoneOffset.UTC);
    return new InstantYearMonth(startInstant, endInstant);
  }

  public record InstantYearMonth(Instant start, Instant end) {

  }
}
