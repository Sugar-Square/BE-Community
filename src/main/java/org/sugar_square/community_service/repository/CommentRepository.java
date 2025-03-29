package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Comment;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long>{

}
