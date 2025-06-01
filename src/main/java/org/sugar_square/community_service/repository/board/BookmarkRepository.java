package org.sugar_square.community_service.repository.board;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Bookmark;
import org.sugar_square.community_service.domain.board.BookmarkPK;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface BookmarkRepository extends BaseRepository<Bookmark, BookmarkPK> {

}
