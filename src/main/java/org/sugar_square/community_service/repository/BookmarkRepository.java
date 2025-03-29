package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Bookmark;

@Repository
public interface BookmarkRepository extends BaseRepository<Bookmark, Long> {

}
