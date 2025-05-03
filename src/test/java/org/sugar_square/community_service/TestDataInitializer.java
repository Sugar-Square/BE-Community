package org.sugar_square.community_service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Comment;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.board.CategoryRepository;
import org.sugar_square.community_service.repository.board.CommentRepository;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.repository.member.MemberRepository;

@Component
@ActiveProfiles("test")
public class TestDataInitializer {

  public static final String MEMBER_USERNAME = "test_username";
  public static final String MEMBER_PASSWORD = "test_password";
  public static final String MEMBER_NICKNAME = "test_nickname";
  public static final String CATEGORY_NAME = "test_category";
  public static final String CATEGORY_DESCRIPTION = "test_description";
  public static final String POST_TITLE = "test_title";
  public static final String POST_CONTENT = "test_content";
  public static final String COMMENT_CONTENT = "test_content";

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private CommentRepository commentRepository;

  private final List<String> tableNames = new ArrayList<>();

  @Getter
  private final List<Member> members = new ArrayList<>();

  @Getter
  private final List<Category> categories = new ArrayList<>();

  @Getter
  private final List<Post> posts = new ArrayList<>();

  @Getter
  private final List<Comment> comments = new ArrayList<>();

  private void clearLists() {
    members.clear();
    categories.clear();
    posts.clear();
    comments.clear();
  }

  public void init() {
    final int DUMMY_COUNT = 10;
    clearLists();
    for (int i = 0; i < DUMMY_COUNT; i++) {
      Member savedMember = memberRepository.save(
          Member.builder()
              .username(MEMBER_USERNAME + i)
              .password(MEMBER_PASSWORD + i)
              .nickname(MEMBER_NICKNAME + i)
              .build()
      );
      members.add(savedMember);

      Category savedCategory = categoryRepository.save(
          Category.builder()
              .name(CATEGORY_NAME + i)
              .description(CATEGORY_DESCRIPTION + i)
              .build()
      );
      categories.add(savedCategory);

      Post savedPost = postRepository.save(
          Post.builder()
              .title(POST_TITLE + i)
              .content(POST_CONTENT + i)
              .writer(savedMember)
              .category(savedCategory)
              .build()
      );
      posts.add(savedPost);

      Comment savedComment = commentRepository.save(
          Comment.builder()
              .content(COMMENT_CONTENT)
              .writer(savedMember)
              .post(savedPost)
              .parent(null)
              .build()
      );
      comments.add(savedComment);
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
