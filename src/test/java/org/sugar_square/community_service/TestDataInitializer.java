package org.sugar_square.community_service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.board.CategoryRepository;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.repository.member.MemberRepository;

@Component
@ActiveProfiles("test")
public class TestDataInitializer {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  private final List<String> tableNames = new ArrayList<>();

  public static final String MEMBER_USERNAME = "test_username";
  public static final String MEMBER_PASSWORD = "test_password";
  public static final String MEMBER_NICKNAME = "test_nickname";
  public static final String CATEGORY_NAME = "test_category";
  public static final String CATEGORY_DESCRIPTION = "test_description";
  public static final String POST_TITLE = "test_title";
  public static final String POST_CONTENT = "test_content";

  public void init() {
    final int DUMMY_COUNT = 10;
    for (int i = 0; i < DUMMY_COUNT; i++) {
      Member savedMember = memberRepository.save(
          Member.builder()
              .username(MEMBER_USERNAME + i)
              .password(MEMBER_PASSWORD + i)
              .nickname(MEMBER_NICKNAME + i)
              .build()
      );
      Category savedCategory = categoryRepository.save(
          Category.builder()
              .name(CATEGORY_NAME + i)
              .description(CATEGORY_DESCRIPTION + i)
              .build()
      );
      Post savedPost = postRepository.save(
          Post.builder()
              .title(POST_TITLE + i)
              .content(POST_CONTENT + i)
              .writer(savedMember)
              .category(savedCategory)
              .build()
      );
    }
  }

  @Transactional
  public void clear() {
    em.clear(); // clear the persistence context
    truncate(); // truncate the tables
  }

  private void truncate() {
    initTableNames();
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate(); // FK constraint off
    for (String tableName : tableNames) {
      em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
    }
    em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate(); // FK constraint on
  }

  private void initTableNames() {
    if (!tableNames.isEmpty()) {
      tableNames.clear();
    }
    findTableNames();
  }

  @SuppressWarnings("unchecked")
  private void findTableNames() {
    List<Object[]> results = em.createNativeQuery("SHOW TABLES").getResultList();
    for (Object[] result : results) {
      String name = (String) result[0];
      tableNames.add(name);
    }
  }
}
