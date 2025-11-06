package net.happykoo.vcs.adapter.out.jpa.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.vcs.domain.user.User;

@Entity(name = "user")
@Table(name = "vcs_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserJpaEntity {
    @Id
    private String id;

    private String name;

    private String profileImageUrl;

    public static UserJpaEntity from(User user) {
        return new UserJpaEntity(user.getId(), user.getName(), user.getProfileImageUrl());
    }

    public User toDomain() {
        return User.builder()
                .id(this.getId())
                .name(this.getName())
                .profileImageUrl(this.getProfileImageUrl())
                .build();
    }
}
