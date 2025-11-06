package net.happykoo.vcs.adapter.out;

import jakarta.transaction.Transactional;
import net.happykoo.vcs.adapter.out.jpa.video.VideoJpaRepository;
import net.happykoo.vcs.common.RedisKeyGenerator;
import net.happykoo.vcs.config.TestRedisConfig;
import net.happykoo.vcs.domain.video.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = TestRedisConfig.class)
@Transactional
@Sql("/net/happykoo/vcs/adapter/out/VideoPersistenceAdapterIntTest.sql")
class VideoPersistenceAdapterIntTest {
    @Autowired
    private VideoPersistenceAdapter videoPersistenceAdapter;

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Autowired
    private RedisCacheManager redisCacheManager;

    @SpyBean
    private VideoJpaRepository videoJpaRepository;

    @Test
    @DisplayName("loadVideo :: 레디스 캐시 제대로 동작하는지 확인")
    void test1() {
        var videoId = "video1";
        for (int i = 0; i < 3; i++) {
            videoPersistenceAdapter.loadVideo(videoId);
        }

        //최초 1번만 JPA 호출
        verify(videoJpaRepository, times(1)).findById(videoId);
    }

    @Test
    @DisplayName("loadVideoByChannel :: 레디스 캐시 제대로 동작하는지 확인")
    void test2() {
        var channelId = "happykoo-channel";

        for (int i = 0; i < 3; i++) {
            videoPersistenceAdapter.loadVideoByChannel(channelId);
        }

        verify(videoJpaRepository, times(1)).findByChannelId(channelId);
    }

    @Test
    @DisplayName("saveVideo :: 비디오 생성되었을 때, redis에 저장된 채널 비디오 리스트 데이터 삭제되는지 확인")
    void test3() {
        // Given
        var channelId = "happykoo-channel";
        // cache 저장
        videoPersistenceAdapter.loadVideoByChannel(channelId);

        then(redisCacheManager.getCache("video:list").get(channelId))
                .isNotNull();


        var video = Video.builder()
                .id("video3")
                .title("video3")
                .description("video3")
                .thumbnailUrl("https://example.com/image.jpg")
                .channelId(channelId)
                .publishedAt(LocalDateTime.now())
                .build();

        // When
        videoPersistenceAdapter.saveVideo(video);

        // Then
        then(redisCacheManager.getCache("video:list").get(channelId))
                .isNull();
    }

    @Test
    @DisplayName("saveVideo :: 비디오 수정되었을때, redis에 비디오 데이터 삭제되는지 확인")
    void test4() {
        // Given
        var videoId = "video1";
        var video = videoPersistenceAdapter.loadVideo(videoId);
        then(redisCacheManager.getCache("video").get(videoId))
                .isNotNull();

        var updatedVideo = Video.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .thumbnailUrl(video.getThumbnailUrl())
                .viewCount(200L)
                .channelId(video.getChannelId())
                .publishedAt(video.getPublishedAt())
                .build();

        // When
        videoPersistenceAdapter.saveVideo(updatedVideo);

        // Then
        then(redisCacheManager.getCache("video").get(videoId))
                .isNull();
    }

    @Test
    @DisplayName("비디오 view count 증가 테스트")
    void test5() {
        var videoId = "video1";

        for (int i = 0; i < 5; i++) {
            videoPersistenceAdapter.incrementViewCount(videoId);
        }

        var result = redisTemplate.opsForValue().get(RedisKeyGenerator.getVideoViewCountKey(videoId));
        then(result).isEqualTo(5L);
    }
}
