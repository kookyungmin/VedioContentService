package net.happykoo.vcs.adapter.out;

import net.happykoo.vcs.adapter.out.jpa.video.VideoJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.video.VideoJpaEntityFixtures;
import net.happykoo.vcs.adapter.out.jpa.video.VideoJpaRepository;
import net.happykoo.vcs.domain.video.VideoFixtures;
import net.happykoo.vcs.exception.DomainNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VideoPersistenceAdapterTest {
    private VideoPersistenceAdapter videoPersistenceAdapter;

    private final VideoJpaRepository videoJpaRepository = mock(VideoJpaRepository.class);
    private final RedisTemplate<String, Long> redisTemplate = mock(RedisTemplate.class, Mockito.RETURNS_DEEP_STUBS);
    private final StringRedisTemplate stringRedisTemplate = mock(StringRedisTemplate.class, Mockito.RETURNS_DEEP_STUBS);
    private final ValueOperations<String, Long> valueOperations = mock(ValueOperations.class);

    @BeforeEach
    void setUp() {
        videoPersistenceAdapter = new VideoPersistenceAdapter(videoJpaRepository, redisTemplate, stringRedisTemplate);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
    }

    @Nested
    class LoadVideo {
        @Test
        @DisplayName("loadVideo :: 정상적으로 조회되는 경우")
        void test1() {
            // Given
            var videoId = "video1";
            var videoJpaEntity = VideoJpaEntityFixtures.stub(videoId);
            given(videoJpaRepository.findById(any()))
                    .willReturn(Optional.of(videoJpaEntity));

            // When
            var result = videoPersistenceAdapter.loadVideo(videoId);

            // Then
            then(result)
                    .extracting("id")
                    .isEqualTo(videoId);
        }

        @Test
        @DisplayName("loadVideo :: 데이터가 없는 경우")
        void test2() {
            // Given
            given(videoJpaRepository.findById(any()))
                    .willReturn(Optional.empty());

            // When
            var result = thenThrownBy(() -> videoPersistenceAdapter.loadVideo("video1"));

            // Then
            result.isInstanceOf(DomainNotFoundException.class);
        }
    }

    @Test
    @DisplayName("loadVideoByChannel :: 데이터가 조회되는 경우")
    void test1() {
        // Given
        var videoJpaEntity1 = VideoJpaEntityFixtures.stub("video1");
        var videoJpaEntity2 = VideoJpaEntityFixtures.stub("video2");
        given(videoJpaRepository.findByChannelId(any()))
                .willReturn(List.of(videoJpaEntity1, videoJpaEntity2));

        // When
        var result = videoPersistenceAdapter.loadVideoByChannel("happykoo-channel");

        // Then
        then(result)
                .hasSize(2)
                .extracting("id")
                .containsExactly("video1", "video2");
    }

    @Test
    @DisplayName("saveVideo :: 데이터가 정상적으로 저장되는 경우")
    void test2() {
        // Given
        var videoId = "videoId";
        var video = VideoFixtures.stub(videoId);
        ArgumentCaptor<VideoJpaEntity> argumentCaptor = ArgumentCaptor.forClass(VideoJpaEntity.class);

        // When
        videoPersistenceAdapter.saveVideo(video);

        // Then
        verify(videoJpaRepository).save(argumentCaptor.capture());
        then(argumentCaptor.getValue())
                .hasFieldOrPropertyWithValue("title", video.getTitle())
                .hasFieldOrPropertyWithValue("description", video.getDescription())
                .hasFieldOrPropertyWithValue("thumbnailUrl", video.getThumbnailUrl())
                .hasFieldOrPropertyWithValue("fileUrl", video.getFileUrl())
                .hasFieldOrPropertyWithValue("channelId", video.getChannelId());
    }

    @Test
    @DisplayName("incrementViewCount :: view count 정상적 증가")
    void test3() {
        given(valueOperations.increment(any())).willReturn(5L);

        videoPersistenceAdapter.incrementViewCount("video1");

        verify(valueOperations).increment("video:view-count:video1");
    }

    @Test
    @DisplayName("incrementViewCount :: view count 정상적 조회")
    void test4() {
        given(redisTemplate.opsForValue().get(any())).willReturn(10L);

        var result = videoPersistenceAdapter.getViewCount("video1");

        then(result).isEqualTo(10L);
    }

    @Test
    @DisplayName("incrementViewCount :: view count redis에 저장 안되어 있으면, 0으로 반환")
    void test5() {
        given(redisTemplate.opsForValue().get(any())).willReturn(null);

        var result = videoPersistenceAdapter.getViewCount("video1");

        then(result).isEqualTo(0L);
    }
}
