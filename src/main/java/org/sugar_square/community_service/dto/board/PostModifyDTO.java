package org.sugar_square.community_service.dto.board;

public record PostModifyDTO(
    String title,
    String content,
    Long categoryId
) {
  
}
