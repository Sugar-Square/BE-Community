package org.sugar_square.community_service.service.board;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.sugar_square.community_service.TestData;
import org.sugar_square.community_service.TestDataInitializer;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.exception.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceTest {

  @Autowired
  private CategoryService categoryService;

  private TestData testData;

  @BeforeEach
  void setup(@Autowired TestDataInitializer initializer) {
    initializer.init();
    testData = new TestData(initializer);
  }

  @AfterEach
  void cleanup(@Autowired TestDataInitializer initializer) {
    initializer.clear();
  }

  @Test
  @DisplayName("카테고리 등록 테스트")
  void registerCategoryTest() {
    // given
    final String newName = "New Test Category";
    final String newDescription = "New Test Category Description";
    // when
    Category savedCategory = categoryService.register(newName, newDescription);
    // then
    Assertions.assertThat(savedCategory)
        .isNotNull()
        .extracting("name", "description")
        .containsExactly(newName, newDescription);
  }

  @Test
  @DisplayName("카테고리 삭제 테스트")
  void removeCategoryTest() {
    // given
    Category category = testData.getCategories().getFirst();
    Long categoryId = category.getId();
    // when
    categoryService.remove(categoryId);
    // then
    Assertions.assertThatThrownBy(() -> categoryService.findOneById(categoryId))
        .isNotNull()
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("category not found: " + categoryId);
  }
}
