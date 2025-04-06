package org.sugar_square.community_service.dto.board;

import java.time.Instant;
import lombok.Builder;
import org.sugar_square.community_service.domain.board.Post;

@Builder
public record PostResponseDTO(
    String title,
    String content,
    String writerNickname, // Member writer 대응
    String categoryName, // Category category 대응
    Long viewCount,
    Instant createdAt,
    Instant updatedAt
) {

  public static PostResponseDTO fromEntity(Post entity) {
    // TODO: 작성자, 카테고리 이름 변환
    return PostResponseDTO.builder()
        .title(entity.getTitle())
        .content(entity.getContent())
        .writerNickname(entity.getWriter().getNickname())
        .categoryName(entity.getCategory().getName())
        .viewCount(entity.getViewCount())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
