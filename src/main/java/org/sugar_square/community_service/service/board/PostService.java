package org.sugar_square.community_service.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostModifyDTO;
import org.sugar_square.community_service.dto.board.PostResponseDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
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
      final String title, final String content, final Long memberId, final Long categoryId
  ) {
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

  @Transactional(readOnly = false)
  public PostResponseDTO readOneById(final Long postId) {
    Post foundPost = findOneById(postId);
    foundPost.increaseViewCount();
    return PostResponseDTO.fromEntity(foundPost);
  }

  @Transactional(readOnly = false)
  public void modify(final Long postId, final PostModifyDTO modifyDTO) {
    Post foundPost = findOneById(postId);
    Category newCategory = categoryService.findOneById(modifyDTO.categoryId());
    foundPost.update(modifyDTO.title(), modifyDTO.content(), newCategory);
  }

  public Post findOneById(final Long postId) {
    return postRepository
        .findById(postId)
        .orElseThrow(() -> new EntityNotFoundException("post not found: " + postId));
  }
}
