package org.sugar_square.community_service.repository.board;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface PostRepository extends BaseRepository<Post, Long>, PostSearch {

}
