package net.happykoo.vcs.adapter.out;

import net.happykoo.vcs.config.TestRedisConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static net.happykoo.vcs.common.RedisKeyGenerator.getVideoLikeKey;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = TestRedisConfig.class)
public class VideoLikePersistenceAdapterIntTest {
    @Autowired
    private VideoLikePersistenceAdapter videoLikePersistenceAdapter;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Nested
    @DisplayName("videoId 로 생성된 VideoLike 키로 userId 를 set에 추가")
    class AddLikeVideo {
        final String videoId = "videoId1";
        final String userId = "userId";

        @Test
        @DisplayName("add 하면 userId 가 추가된다")
        void test1() {
            videoLikePersistenceAdapter.addVideoLike(videoId, userId);

            then(redisTemplate.opsForSet().isMember(getVideoLikeKey(videoId), userId))
                    .isTrue();
            then(redisTemplate.opsForSet().size(getVideoLikeKey(videoId)))
                    .isEqualTo(1L);
        }

        @Test
        @DisplayName("여러번 add 해도 userId 가 중복 추가되지 않는다")
        void test2() {
            for(int i = 0; i < 5; i++) {
                videoLikePersistenceAdapter.addVideoLike(videoId, userId);
            }

            then(redisTemplate.opsForSet().size(getVideoLikeKey(videoId)))
                    .isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("videoId 로 생성된 VideoLike 키로 userId 를 set에서 제거")
    class RemoveLikeVideo {
        final String videoId = "videoId2";
        final String userId = "userId";

        @Test
        @DisplayName("remove 하면 userId 가 제거된다")
        void test1() {
            videoLikePersistenceAdapter.addVideoLike(videoId, userId);

            then(redisTemplate.opsForSet().isMember(getVideoLikeKey(videoId), userId))
                    .isTrue();

            videoLikePersistenceAdapter.removeVideoLike(videoId, userId);

            then(redisTemplate.opsForSet().isMember(getVideoLikeKey(videoId), userId))
                    .isFalse();
        }
    }

    @Nested
    @DisplayName("videoId 로 생성된 VideoLike 키로 user 수 count")
    class CountLikeVideo {
        final String videoId = "videoId3";
        final String userId = "userId";

        @Test
        @DisplayName("add 한 user 수 만큼 count 값 반환")
        void test1() {
            final int loopCount = 10;
            for (int i = 0; i < loopCount; i++) {
                redisTemplate.opsForSet().add(getVideoLikeKey(videoId), userId + i);
            }

            var result = videoLikePersistenceAdapter.getVideoLikeCount(videoId);

            then(result).isEqualTo(loopCount);
        }
    }
}
