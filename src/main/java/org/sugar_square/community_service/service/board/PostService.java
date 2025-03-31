package org.sugar_square.community_service.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.board.PostRepository;
import org.sugar_square.community_service.service.member.MemberService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final CategoryService categoryService;
  private final MemberService memberService;

  @Transactional(readOnly = false)
  public Long register(
      final String title,
      final String content,
      final Long memberId,
      final Long categoryId) {
    Member writer = memberService.findOneById(memberId);
    Category category = categoryService.findOneById(categoryId);
    Post registered = Post.builder()
        .title(title)
        .content(content)
        .writer(writer)
        .category(category)
        .build();
    Post result = postRepository.save(registered);
    return result.getId();
  }
}
