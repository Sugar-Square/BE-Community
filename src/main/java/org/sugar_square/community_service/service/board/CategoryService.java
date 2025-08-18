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