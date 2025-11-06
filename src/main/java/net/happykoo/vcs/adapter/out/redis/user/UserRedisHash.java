package net.happykoo.vcs.adapter.out.redis.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.happykoo.vcs.domain.user.User;
import org.springframework.data.redis.core.RedisHash;

import static net.happykoo.vcs.common.CacheNames.USER;

@RedisHash(value = USER)
@AllArgsConstructor
@Getter
public class UserRedisHash {
    private String id;
    private String name;
    private String profileImageUrl;

    public static UserRedisHash from(User user) {
        return new UserRedisHash(user.getId(), user.getName(), user.getProfileImageUrl());
    }

    public User toDomain() {
        return User.builder()
                .id(this.getId())
                .name(this.getName())
                .profileImageUrl(this.getProfileImageUrl())
                .build();
    }
}
