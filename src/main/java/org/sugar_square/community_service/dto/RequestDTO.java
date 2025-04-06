package org.sugar_square.community_service.dto;

public interface RequestDTO<T> {

  // TODO: 컴파일 단계에서 타입체크 못하는 문제 해결해야 함
  public T dtoToEntity(Object... args);
}
