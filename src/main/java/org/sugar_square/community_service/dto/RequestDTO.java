package org.sugar_square.community_service.dto;

public interface RequestDTO<T> {

  public T dtoToEntity(Object... args);

  public void updateEntity();
}
