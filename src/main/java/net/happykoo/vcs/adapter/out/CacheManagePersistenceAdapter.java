package net.happykoo.vcs.adapter.out;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.out.CacheManagePort;
import net.happykoo.vcs.common.CacheNames;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CacheManagePersistenceAdapter implements CacheManagePort {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> getAllCacheNames() {
        // 운영 환경에서는 절대 사용하면 안되는 명령어 keys *
        // stringRedisTemplate.keys("*").stream().toList();
        return CacheNames.getCacheNames();
    }

    @Override
    @Deprecated
    public List<String> getAllCacheNames(String pattern) {
//        패턴이 있더라도 keys 명령어 사용 지양
        var keys = stringRedisTemplate.keys(pattern + "*");
        if (keys == null) {
            return Collections.emptyList();
        }
        return keys.stream().toList();
    }

    @Override
    public boolean deleteCache(String cacheKey) {
        return stringRedisTemplate.delete(cacheKey);
    }
}
