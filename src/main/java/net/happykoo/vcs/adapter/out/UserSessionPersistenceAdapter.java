package net.happykoo.vcs.adapter.out;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.out.UserSessionPort;
import net.happykoo.vcs.common.RedisKeyGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSessionPersistenceAdapter implements UserSessionPort {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public String getUserId(String authKey) {
        return stringRedisTemplate.opsForValue()
                .get(RedisKeyGenerator.getUserSessionKey(authKey));
    }
}
