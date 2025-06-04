package org.sugar_square.community_service.service.board;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sugar_square.community_service.controller.board.BookmarkController.BookmarkResult;
import org.sugar_square.community_service.domain.board.Bookmark;
import org.sugar_square.community_service.domain.board.BookmarkPK;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;
import org.sugar_square.community_service.dto.PageResponseDTO;
import org.sugar_square.community_service.dto.board.BookmarkResponseDTO;
import org.sugar_square.community_service.exception.EntityNotFoundException;
import org.sugar_square.community_service.repository.board.BookmarkRepository;
import org.sugar_square.community_service.service.member.MemberService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final PostService postService;
  private final MemberService memberService;

  public PageResponseDTO<BookmarkResponseDTO> findAllByMemberId(
      final Long memberId,
      final Pageable pageable
  ) {
    // get pages
    Page<Bookmark> pages = bookmarkRepository.findAllByBookmarkPK_MemberId(
        memberId, pageable);
    // extract content and convert to dto
    List<BookmarkResponseDTO> bookmarkDTOList = pages
        .getContent()
        .stream()
        .map(bookmark -> BookmarkResponseDTO.fromEntity(bookmark.getBookmarkPK().getPost()))
        .toList();
    // convert to page response dto and return
    return PageResponseDTO.of(pageable, bookmarkDTOList, (int) pages.getTotalElements());
  }

  @Transactional
  public BookmarkResult register(final Long memberId, final Long postId) {
    Member member = memberService.findOneById(memberId);
    Post post = postService.findOneById(postId);
    BookmarkPK pk = new BookmarkPK(post, member);
    if (bookmarkRepository.existsById(pk)) {
      throw new IllegalArgumentException("Already bookmarked this post.");
    }
    Bookmark bookmarked = bookmarkRepository.save(new Bookmark(pk));
    BookmarkPK bookmarkedPK = bookmarked.getBookmarkPK();
    return new BookmarkResult(bookmarkedPK.getMember().getId(), bookmarkedPK.getPost().getId());
  }

  @Transactional
  public void remove(final Long memberId, final Long postId) {
    Member member = memberService.findOneById(memberId);
    Post post = postService.findOneById(postId);
    Bookmark foundBookmark = findOneById(new BookmarkPK(post, member));
    bookmarkRepository.delete(foundBookmark);
  }

  public Bookmark findOneById(final BookmarkPK pk) {
    return bookmarkRepository
        .findById(pk)
        .orElseThrow(() -> new EntityNotFoundException("Bookmark not found"));
  }
}
