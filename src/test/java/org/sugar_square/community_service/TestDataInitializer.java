package org.sugar_square.community_service;

import static org.sugar_square.community_service.enums.RoleEnum.ROLE_USER;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.repository.board.CategoryRepository;
import org.sugar_square.community_service.repository.board.CommentRepository;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.repository.member.MemberRepository;
import org.sugar_square.community_service.repository.schedule.ScheduleRepository;
import org.sugar_square.community_service.utils.StringDateConverter;

@Component
@ActiveProfiles("test")
public class TestDataInitializer {

  public static final String MEMBER_USERNAME = "test_username";
  public static final String MEMBER_PASSWORD = "test_password";
  public static final String MEMBER_NICKNAME = "test_nickname";
  public static final String MEMBER_NAME = "test_name";
  public static final String MEMBER_BIRTHDAY = "2025-01-01"; // format: yyyy-MM-dd
  public static final String MEMBER_EMAIL = "test@gmail.com"; // format: yyyy-MM-dd
  public static final String CATEGORY_NAME = "test_category";
  public static final String CATEGORY_DESCRIPTION = "test_description";
  public static final String POST_TITLE = "test_title";
  public static final String POST_CONTENT = "test_content";
  public static final String COMMENT_CONTENT = "test_content";
  public static final String SCHEDULE_TITLE = "test_schedule_title";
  public static final String SCHEDULE_CONTENT = "test_schedule_content";
  public static final String SCHEDULE_DATE = "2025-08-01T00:00:00Z"; // format: yyyy-MM-dd'T'HH:mm:ss'Z'
  public static final String SCHEDULE_NOTIFICATION_DATE = "2025-07-31T00:00:00Z"; // format: yyyy-MM-dd'T'HH:mm:ss'Z'
  public static final int DUMMY_COUNT = 10;

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

  @Autowired
  private ScheduleRepository scheduleRepository;

  private final List<String> tableNames = new ArrayList<>();

  @Getter
  private final List<Member> members = new ArrayList<>();

  @Getter
  private final List<Category> categories = new ArrayList<>();

  @Getter
  private final List<Post> posts = new ArrayList<>();

  @Getter
  private final List<Comment> comments = new ArrayList<>();

  @Getter
  private final List<Schedule> schedules = new ArrayList<>();

  private void clearLists() {
    members.clear();
    categories.clear();
    posts.clear();
    comments.clear();
    schedules.clear();
  }

  public void init() {
    clearLists();
    for (int i = 0; i < DUMMY_COUNT; i++) {
      Member savedMember = createMember(i);
      Category savedCategory = createCategory(i);
      for (int j = 0; j < DUMMY_COUNT; j++) {
        Post savedPost = createPost(j, savedMember, savedCategory);
        Schedule savedSchedule = createSchedule(j, savedMember);
        for (int k = 0; k < DUMMY_COUNT; k++) {
          Comment savedComment = createComment(k, savedMember, savedPost);
        }
      }
    }
  }

  private Schedule createSchedule(final int j, final Member savedMember) {
    Schedule savedSchedule = scheduleRepository.save(
        Schedule.builder()
            .title(SCHEDULE_TITLE + j)
            .content(SCHEDULE_CONTENT + j)
            .scheduleDate(StringDateConverter.stringToInstant(SCHEDULE_DATE))
            .notificationDate(StringDateConverter.stringToInstant(SCHEDULE_NOTIFICATION_DATE))
            .writer(savedMember)
            .build()
    );
    schedules.add(savedSchedule);
    return savedSchedule;
  }

  private Comment createComment(final int k, final Member savedMember, final Post savedPost) {
    Comment savedComment = commentRepository.save(
        Comment.builder()
            .content(COMMENT_CONTENT + k)
            .writer(savedMember)
            .post(savedPost)
            .parent(null)
            .build()
    );
    comments.add(savedComment);
    return savedComment;
  }

  private Post createPost(final int j, final Member savedMember, final Category savedCategory) {
    Post savedPost = postRepository.save(
        Post.builder()
            .title(POST_TITLE + j)
            .content(POST_CONTENT + j)
            .writer(savedMember)
            .category(savedCategory)
            .build()
    );
    posts.add(savedPost);
    return savedPost;
  }

  private Category createCategory(final int i) {
    Category savedCategory = categoryRepository.save(
        Category.builder()
            .name(CATEGORY_NAME + i)
            .description(CATEGORY_DESCRIPTION + i)
            .build()
    );
    categories.add(savedCategory);
    return savedCategory;
  }

  private Member createMember(final int i) {
    Member savedMember = memberRepository.save(
        Member.builder()
            .username(MEMBER_USERNAME + i)
            .password(MEMBER_PASSWORD + i)
            .nickname(MEMBER_NICKNAME + i)
            .role(ROLE_USER)
            // nullable
            .name(MEMBER_NAME + i)
            .birthday(LocalDate.parse(MEMBER_BIRTHDAY, DateTimeFormatter.ISO_LOCAL_DATE))
            .email(MEMBER_EMAIL)
            .build()
    );
    members.add(savedMember);
    return savedMember;
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
