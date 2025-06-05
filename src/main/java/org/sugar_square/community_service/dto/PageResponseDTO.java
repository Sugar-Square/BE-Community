package org.sugar_square.community_service.dto;

import java.util.List;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

@Getter
@ToString
public class PageResponseDTO<E> {

  private static final int PAGE_RANGE = 5; // number of pages to display
  private int page; // current page number
  private int size; // content size per page
  private int total; // total content count
  private int start; // page start number
  private int end; // page end number
  private int last; // last page number
  private boolean prev; // is previous page existed?
  private boolean next; // is next page existed?
  private List<E> dtoList; // content list

  public static <E> PageResponseDTO<E> of(Pageable pageable, List<E> dtoList, int total) {
    return new PageResponseDTO<>(pageable, dtoList, total);
  }

  private PageResponseDTO(Pageable pageable, List<E> dtoList, int total) {

    if (total <= 0) { // no content
      return;
    }

    // TODO : start, end, last 등 계산식 세부 조정
    this.page = pageable.getPageNumber() + 1;
    this.size = pageable.getPageSize();
    this.total = total;
    this.dtoList = dtoList;
    this.end = (int) (Math.ceil(this.page / (double) PAGE_RANGE)) * PAGE_RANGE;
    this.start = Math.max(1, this.end - (PAGE_RANGE - 1));
    this.last = (int) (Math.ceil((total / (double) size)));
    this.end = Math.min(end, last);
    this.prev = this.start > 1;
    this.next = total > this.end * this.size;
  }
}
