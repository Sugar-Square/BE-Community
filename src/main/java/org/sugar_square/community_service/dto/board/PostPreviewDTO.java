package org.sugar_square.community_service.dto.board;

import java.time.Instant;
import lombok.Builder;
import org.sugar_square.community_service.domain.board.Post;

@Builder
public record PostPreviewDTO(
    Long postId,
    Long writerId,
    Long categoryId,
    String title,
    String writerNickname,
    Long viewCount,
    Instant createdAt
) {

  public static PostPreviewDTO fromEntity(Post entity) {
    return PostPreviewDTO.builder()
        .postId(entity.getId())
        .writerId(entity.getWriter().getId())
        .categoryId(entity.getCategory().getId())
        .title(entity.getTitle())
        .writerNickname(entity.getWriter().getNickname())
        .viewCount(entity.getViewCount())
        .createdAt(entity.getCreatedAt())
        .build();
  }
}
