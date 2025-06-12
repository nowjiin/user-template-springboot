package com.example.usertemplate.global.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
  private List<T> content;
  private int page;
  private int size;
  private long totalElements;
  private int totalPages;
  private boolean first;
  private boolean last;
  private boolean empty;

  public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
    int totalPages = (int) Math.ceil((double) totalElements / size);
    boolean isFirst = page == 0;
    boolean isLast = page >= totalPages - 1;
    boolean isEmpty = content.isEmpty();

    return new PageResponse<>(
        content, page, size, totalElements, totalPages, isFirst, isLast, isEmpty);
  }
}
