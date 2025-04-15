package org.sugar_square.community_service.repository.board;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {

  List<Category> findByName(String name);
}
