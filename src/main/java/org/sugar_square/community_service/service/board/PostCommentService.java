package org.sugar_square.community_service.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Comment;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.board.CommentRepository;
import org.sugar_square.community_service.service.member.MemberService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentService {

  private final PostService postService;
  private final MemberService memberService;
  private final CommentRepository commentRepository;

  @Transactional
  public Long register(final Long postId, final PostCommentRegisterDTO registerDTO) {
    Post foundPost = postService.findOneById(postId);
    Member writer = memberService.findOneById(registerDTO.memberId());
    Comment parent = null;
    if (registerDTO.parentId() != null) {
      parent = findOneById(registerDTO.parentId());
    }
    Comment registered = Comment.builder()
        .content(registerDTO.content())
        .writer(writer)
        .post(foundPost)
        .parent(parent) // nullable
        .build();
    Comment result = commentRepository.save(registered);
    return result.getId();
  }

  public Comment findOneById(final Long commentId) {
    return commentRepository
        .findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("comment not found: " + commentId));
  }
}
