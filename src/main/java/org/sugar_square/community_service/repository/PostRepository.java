package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Post;

@Repository
public interface PostRepository extends BaseRepository<Post, Long>{

}
