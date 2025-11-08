package net.happykoo.vcs.adapter.out;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.out.BlockCommentPort;
import net.happykoo.vcs.common.RedisKeyGenerator;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommentBlockPersistenceAdapter implements BlockCommentPort {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveUserCommentBlock(String userId, String commentId) {
        stringRedisTemplate.opsForSet().add(RedisKeyGenerator.getCommentLikeKey(RedisKeyGenerator.getUserCommentBlock(userId)));
    }

    @Override
    public Set<String> getUserCommentBlocks(String userId) {
        return stringRedisTemplate.opsForSet().members(userId);
    }
}
