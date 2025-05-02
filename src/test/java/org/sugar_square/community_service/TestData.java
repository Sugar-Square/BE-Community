package org.sugar_square.community_service;

import java.util.List;
import lombok.Getter;
import org.sugar_square.community_service.domain.board.Category;
import org.sugar_square.community_service.domain.board.Comment;
import org.sugar_square.community_service.domain.board.Post;
import org.sugar_square.community_service.domain.member.Member;

@Getter
public class TestData {

  private final List<Member> members;
  private final List<Category> categories;
  private final List<Post> posts;
  private final List<Comment> comments;

  public TestData(TestDataInitializer initializer) {
    this.members = initializer.getMembers();
    this.categories = initializer.getCategories();
    this.posts = initializer.getPosts();
    this.comments = initializer.getComments();
  }
}
