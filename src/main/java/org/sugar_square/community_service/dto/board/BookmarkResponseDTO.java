package org.sugar_square.community_service.dto.board;

import java.time.Instant;
import lombok.Builder;
import org.sugar_square.community_service.domain.board.Post;

@Builder
public record BookmarkResponseDTO(
    Long postId,
    Long memberId,
    Long categoryId,
    String title, // 게시글 제목
    String writer, // 게시글 작성자
    String categoryName,
    Instant postCreatedAt // 게시글 작성 시간
) {

  public static BookmarkResponseDTO fromEntity(final Post entity) {
    return BookmarkResponseDTO.builder()
        .postId(entity.getId())
        .memberId(entity.getWriter().getId())
        .categoryId(entity.getCategory().getId())
        .title(entity.getTitle())
        .writer(entity.getWriter().getNickname())
        .categoryName(entity.getCategory().getName())
        .postCreatedAt(entity.getCreatedAt())
        .build();
  }
}
