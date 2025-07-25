package org.sugar_square.community_service.dto.board;

import java.time.Instant;
import lombok.Builder;
import org.sugar_square.community_service.domain.board.Comment;

@Builder
public record PostCommentResponseDTO(
    Long id,
    Long parentId,
    Long postId,
    Long memberId,
    String content,
    // 이하는 삭제된 comment 의 경우 null
    String writerNickname,
    Instant createdAt,
    Instant updatedAt
) {

  public static PostCommentResponseDTO fromEntity(final Comment entity) {
    return entity.isDeleted() ? removedCommentToDTO(entity) : commentToDTO(entity);
  }

  private static PostCommentResponseDTO commentToDTO(final Comment entity) {
    return PostCommentResponseDTO.builder()
        .id(entity.getId())
        .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
        .postId(entity.getPost().getId())
        .memberId(entity.getWriter().getId())
        .content(entity.getContent())
        .writerNickname(entity.getWriter().getNickname())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  private static PostCommentResponseDTO removedCommentToDTO(final Comment entity) {
    final String DELETED_MESSAGE = "삭제된 댓글입니다";
    return PostCommentResponseDTO.builder()
        .id(entity.getId())
        .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
        .postId(entity.getPost().getId())
        .content(DELETED_MESSAGE)
        .build();
  }
}