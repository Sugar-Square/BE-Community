package org.sugar_square.community_service.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.sugar_square.community_service.controller.board.PostController.SearchCondition;
import org.sugar_square.community_service.domain.board.Post;

public interface PostSearch {

  Page<Post> searchAll(Long categoryId, SearchCondition searchCondition, Pageable pageable);
}
