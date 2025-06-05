package org.sugar_square.community_service.dto.board;


import jakarta.annotation.Nonnull;

public record PostCommentRegisterDTO(
    String content,
    Long parentId, // nullable
    @Nonnull Long memberId
) {

}
