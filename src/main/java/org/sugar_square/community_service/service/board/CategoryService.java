package org.sugar_square.community_service.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.board.CategoryRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Transactional
  public Category register(final String name, final String description) {
    checkDuplication(name);
    Category newCategory = Category.builder()
        .name(name)
        .description(description)
        .build();
    return categoryRepository.save(newCategory);
  }

  @Transactional
  public void remove(final Long categoryId) {
    if (!categoryRepository.existsById(categoryId)) {
      throw new EntityNotFoundException("Category not found : " + categoryId);
    }
    categoryRepository.softDeleteById(categoryId);
    /* TODO : 게시글이 존재하는 카테고리를 삭제하는경우 이후 로직에서 어떻게 처리할 지 고민
     * <카테고리 삭제 후 예상 동작>
     * 1. 게시글등록, 수정, 검색 등 해당 게시글의 카테고리 엔티티를 조회해야하는 로직의 실패. (아마도 해당 category 엔티티를 찾을 수 없다는 exception 문구 출력)
     * 2. 클라이언트에서는 삭제된 카테고리가 표시되지 않지만, 카테고리의 게시글들은 DB에서 삭제 처리되지 않은 상태
     * 3. 게시글은 삭제 x -> post id 로 해당 게시글을 조회 가능
     *
     * <예상되는 동작을 고려한 추가적인 로직>
     * 1. 게시글을 모두 삭제처리 하는건 오버헤드가 너무 큼 -> 게시글은 삭제(soft delete) 하지 않기로 결정
     * 2. 삭제된 카테고리는 클라이언트에서 조회되지 않도록 처리 (해당 게시글의 조회도 불가)
     * 3. 카테고리와 언링크된 게시글들을 조회만 할 수 있도록 "아카이브" 처리를 고려
     * 4. 만약 아카이브 처리하지 않는다면, 카테고리가 삭제된 게시글에 대한 접근을 제한하도록 할 지 고민해야 함
     * */
  }

  public Category findOneById(final Long categoryId) {
    return categoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new EntityNotFoundException("category not found: " + categoryId));
  }

  public void checkDuplication(final String name) {
    if (categoryRepository.existsByName(name)) {
      throw new IllegalArgumentException("Duplication check failed: category name already exists");
    }
  }
}