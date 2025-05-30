package org.sugar_square.community_service.service.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.domain.board.Bookmark;
import org.sugar_square.community_service.domain.board.BookmarkPK;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.repository.board.BookmarkRepository;
import org.sugar_square.community_service.service.member.MemberService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final PostService postService;
  private final MemberService memberService;

  @Transactional
  public BookmarkResult register(final @Valid Long memberId, final @Valid Long postId) {
    if (bookmarkRepository.existsByBookmarkPK_MemberIdAndBookmarkPK_PostId(memberId, postId)) {
      throw new IllegalArgumentException("Already bookmarked this post.");
    }
    Member member = memberService.findOneById(memberId);
    Post post = postService.findOneById(postId);
    Bookmark bookmarked = bookmarkRepository.save(new Bookmark(new BookmarkPK(post, member)));
    BookmarkPK pk = bookmarked.getBookmarkPK();
    return new BookmarkResult(pk.getMember().getId(), pk.getPost().getId());
  }

  public record BookmarkResult(
      Long memberId, Long postId
  ) {

  }
}
