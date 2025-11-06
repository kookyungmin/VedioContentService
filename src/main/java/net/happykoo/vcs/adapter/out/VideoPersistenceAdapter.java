package net.happykoo.vcs.adapter.out;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.out.jpa.video.VideoJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.video.VideoJpaRepository;
import net.happykoo.vcs.application.port.out.LoadVideoPort;
import net.happykoo.vcs.application.port.out.SaveVideoPort;
import net.happykoo.vcs.domain.video.Video;
import net.happykoo.vcs.exception.DomainNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static net.happykoo.vcs.common.CacheNames.VIDEO;
import static net.happykoo.vcs.common.CacheNames.VIDEO_LIST;
import static net.happykoo.vcs.common.RedisKeyGenerator.getVideoViewCountKey;

@Component
@RequiredArgsConstructor
public class VideoPersistenceAdapter implements LoadVideoPort, SaveVideoPort {
    private final VideoJpaRepository videoJpaRepository;
    private final RedisTemplate<String, Long> redisTemplate;

    @Override
    @Cacheable(cacheManager = "redisCacheManager", cacheNames = VIDEO, key = "#videoId")
    public Video loadVideo(String videoId) {
        return videoJpaRepository.findById(videoId)
                .map(VideoJpaEntity::toDomain)
                .orElseThrow(DomainNotFoundException::new);
    }

    @Override
    @Cacheable(cacheManager = "redisCacheManager", cacheNames = VIDEO_LIST, key = "#channelId")
    public List<Video> loadVideoByChannel(String channelId) {
        return videoJpaRepository.findByChannelId(channelId)
                .stream()
                .map(VideoJpaEntity::toDomain)
                .toList();
    }

    @Override
    //evict -> cache에서 삭제
    @Caching(evict = {
        @CacheEvict(cacheNames = VIDEO, key = "#video.id"),
        @CacheEvict(cacheNames = VIDEO_LIST, key = "#video.channelId")
    })
    public void saveVideo(Video video) {
        videoJpaRepository.save(VideoJpaEntity.from(video));
    }

    @Override
    public void incrementViewCount(String videoId) {
        var key = getVideoViewCountKey(videoId);
        redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Long getViewCount(String videoId) {
        var key = getVideoViewCountKey(videoId);
        var viewCount = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(viewCount)
                .orElse(0L);
    }
}
