package org.sugar_square.community_service.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRegisterDTO(
    @NotNull @NotBlank
    String name,
    String description
) {

}
