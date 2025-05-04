package org.sugar_square.community_service.service.board;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

  // TODO: deletedAt != null -> 조회 시 "삭제된 댓글입니다"로 처리
  public List<PostCommentResponseDTO> readAllByPostId(final Long postId) {
    final List<Comment> allComments = new ArrayList<>();
    List<Comment> topLvComments = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAt(
        postId);
    topLvComments.forEach(comment -> {
      allComments.add(comment);
      allComments.addAll(getChildrenRecursively(comment));
    });
    return allComments.stream()
        .map(PostCommentResponseDTO::fromEntity)
        .collect(Collectors.toList());
  }

  private List<Comment> getChildrenRecursively(Comment parent) {
    List<Comment> children = new ArrayList<>();
    for (Comment child : parent.getChildren()) { // 자식이 없으면 재귀 종료
      children.add(child);
      children.addAll(getChildrenRecursively(child));
    }
    return children;
  }

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

  @Transactional
  public void modify(final Long commentId, final PostCommentModifyDTO modifyDTO) {
    Comment foundComment = findOneById(commentId);
    foundComment.update(modifyDTO.content());
  }

  @Transactional
  public void remove(final Long commentId) {
    Comment foundComment = findOneById(commentId);
    commentRepository.softDeleteById(foundComment.getId());
  }

  public Comment findOneById(final Long commentId) {
    return commentRepository
        .findById(commentId)
        .orElseThrow(() -> new EntityNotFoundException("comment not found: " + commentId));
  }
}