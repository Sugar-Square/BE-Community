package org.sugar_square.community_service.domain.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.sugar_square.community_service.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id", nullable = false, updatable = false)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;  // TODO: DTO 에서 한 번 더 null 검증

  @Column(nullable = false)
  private String password; // TODO: DTO 에서 한 번 더 null 검증

  @Column(nullable = false, unique = true)
  private String nickname; // TODO: DTO 에서 한 번 더 null 검증

  private String name;

  private LocalDate birthday;

  private String email;

  @Builder
  private Member(String username, String password, String nickname, String name, LocalDate birthday,
      String email) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.name = name;
    this.birthday = birthday;
    this.email = email;
  }
}
