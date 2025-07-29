package org.sugar_square.community_service.dto.member;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.sugar_square.community_service.utils.StringDateConverter;

public record SignUpRequestDTO(
    @NotNull(message = "You must input a username")
    String username,
    @NotNull(message = "You must input a password")
    String password,
    @NotNull(message = "You must input a nickname")
    String nickname,
    String name,
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    String birthday,
    @Email(message = "You must input a valid email address")
    String email
) {

  @Nullable
  public LocalDate getLocalDateBirthday() {
    return StringDateConverter.stringToLocalDate(birthday);
  }
}
