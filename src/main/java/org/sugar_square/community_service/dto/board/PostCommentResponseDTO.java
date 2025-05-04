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
        .content(entity.getContent())
        .writerNickname(entity.getWriter().getNickname())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }

  private static PostCommentResponseDTO removedCommentToDTO(final Comment entity) {
    String deleteMessage = "삭제된 댓글입니다";
    return PostCommentResponseDTO.builder()
        .id(entity.getId())
        .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
        .postId(entity.getPost().getId())
        .content(deleteMessage)
        .build();
  }

}