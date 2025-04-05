package org.sugar_square.community_service.dto.board;

import lombok.NonNull;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.RequestDTO;

public record PostRequestDTO(
    String title,
    String content,
    @NonNull Long memberId,
    @NonNull Long categoryId
) implements RequestDTO<Post> {

  /**
   * 새로운 엔티티를 등록할 경우만 사용.
   */
  @Override
  public Post dtoToEntity(Object... args) {
    Member writer = (Member) args[0];
    Category category = (Category) args[1];
    return Post.builder()
        .title(title)
        .content(content)
        .writer(writer)
        .category(category)
        .build();
  }

  @Override
  public void updateEntity() {
    // TODO: post entity update 메서드 작성
  }
}
