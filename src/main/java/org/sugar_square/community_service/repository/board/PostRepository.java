package org.sugar_square.community_service.repository.board;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface PostRepository extends BaseRepository<Post, Long> {

  List<Post> findByTitle(String title);
}
