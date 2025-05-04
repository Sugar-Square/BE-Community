package org.sugar_square.community_service.dto.board;

import java.time.Instant;
import lombok.Builder;
import org.sugar_square.community_service.domain.board.Comment;

@Builder
public record PostCommentResponseDTO(
    Long id,
    Long parentId,
    Long postId, // TODO: 필요한 속성인지 검토
    String content,
    String writerNickname,
    Instant createdAt,
    Instant updatedAt
) {

  public static PostCommentResponseDTO fromEntity(Comment entity) {
    // TODO: 작성자, 카테고리 이름 변환
    return PostCommentResponseDTO.builder()
        .id(entity.getId())
        .parentId(entity.getParent().getId())
        .postId(entity.getPost().getId())
        .content(entity.getContent())
        .writerNickname(entity.getWriter().getNickname())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}