package org.sugar_square.community_service.service.board;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Comment;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.board.PostCommentModifyDTO;
import org.sugar_square.community_service.dto.board.PostCommentRegisterDTO;
import org.sugar_square.community_service.dto.board.PostCommentResponseDTO;
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

  // 삭제된 댓글 조회 시 "삭제된 댓글입니다"로 처리
  public List<PostCommentResponseDTO> readAllByPostId(final Long postId) {
    return commentRepository.findByPostIdAndParentIsNullOrderByCreatedAt(postId).stream()
        .flatMap(comment ->
            Stream.concat(Stream.of(comment), getChildrenRecursively(comment).stream())
        )
        .map(PostCommentResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }

  /**
   * 재귀적으로 댓글의 자식 댓글들을 조회
   */
  private List<Comment> getChildrenRecursively(Comment parent) {
    // parent 의 child 가 없으면 getChildrenRecursively() 가 호출되지 못하고 빈 List 를 반환하며 재귀 종료
    return parent.getChildren().stream()
        .flatMap(child ->
            Stream.concat(Stream.of(child), getChildrenRecursively(child).stream())
        )
        .collect(Collectors.toList());
  }

  @Transactional
  public Long register(final Long postId, final PostCommentRegisterDTO registerDTO) {
    Post foundPost = postService.findOneById(postId);
    Member writer = memberService.findOneById(registerDTO.memberId());
    Comment parent = null;
    if (registerDTO.parentId() != null) {
      Comment tempComment = findOneById(registerDTO.parentId());
      // postId 의 포스트에 parentId 가 속해있는지 검증
      if (!tempComment.getPost().getId().equals(postId)) {
        throw new IllegalArgumentException(
            "Parent comment does not belong to the post by post id.");
      }
      parent = tempComment;
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

  @Transactional
  public void modify(
      final Long postId,
      final Long commentId,
      final PostCommentModifyDTO modifyDTO
  ) {
    Comment foundComment = findOneById(commentId);
    if (!foundComment.getPost().getId().equals(postId)) {
      throw new IllegalArgumentException("Comment does not belong to the post by post id.");
    }
    foundComment.update(modifyDTO.content());
  }

  @Transactional
  public void remove(final Long postId, final Long commentId) {
    Comment foundComment = findOneById(commentId);
    if (!foundComment.getPost().getId().equals(postId)) {
      throw new IllegalArgumentException("Comment does not belong to the post by post id.");
    }
    commentRepository.softDeleteById(foundComment.getId());
  }

  public Comment findOneById(final Long commentId) {
    return commentRepository
        .findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("comment not found: " + commentId));
  }
}