package net.happykoo.vcs.adapter.out;

import net.happykoo.vcs.adapter.in.api.dto.ChannelSnippetRequest;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaRepository;
import net.happykoo.vcs.adapter.out.redis.channel.ChannelRedisHash;
import net.happykoo.vcs.adapter.out.redis.channel.ChannelRedisRepository;
import net.happykoo.vcs.config.TestRedisConfig;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestRedisConfig.class)
@Transactional
@Sql("classpath:/net/happykoo/vcs/adapter/out/ChannelPersistenceAdapterIntTest.sql")
class ChannelPersistenceAdapterIntTest {
    @Autowired
    private ChannelPersistenceAdapter channelPersistenceAdapter;

    //실제 빈으로 등록하되 특정 메서드를 mocking 할 수 있음 -> 특정 메서드가 몇번 호출되었는지 테스트 가능
    @SpyBean
    private ChannelJpaRepository channelJpaRepository;

    @SpyBean
    private ChannelRedisRepository channelRedisRepository;

    @BeforeEach
    void setUp() {
        channelRedisRepository.deleteAll();
    }

    @Nested
    @DisplayName("loadChannel 테스트")
    class loadChannelTest {
        @Test
        @DisplayName("Redis cache에서 찾을 수 없으면, JPA 에서 찾음")
        void test1() {
            //given
            var channelId = "happykoo";

            //when
            for (int i = 0; i < 3; i++) {
                channelPersistenceAdapter.loadChannel(channelId);
            }

            //then
            verify(channelJpaRepository, times(1)).findById(channelId); //JPA 1번 호출
            verify(channelRedisRepository, times(3)).findById(channelId); //Redis 1번 호출
        }

        @Test
        @DisplayName("Redis cache에서 찾을 수 있으면, JPA 에서 찾지 않음")
        void test2() {
            //given
            var channelId = "happykoo";
            var channel = ChannelFixtures.stub(channelId);
            channelRedisRepository.save(ChannelRedisHash.from(channel));

            //when
            for (int i = 0; i < 3; i++) {
                channelPersistenceAdapter.loadChannel(channelId);
            }

            //then
            verify(channelJpaRepository,  never()).findById(channelId); //JPA 1번 호출
            verify(channelRedisRepository, times(3)).findById(channelId); //Redis 1번 호출
        }
    }

    @Nested
    @DisplayName("saveChannel 테스트")
    class saveChannel {
        @Test
        @DisplayName("create 된 후 Redis 에 저장 테스트")
        void test1() {
            var channelId = "happykoo";
            var channel = ChannelFixtures.stub(channelId);
            channelPersistenceAdapter.saveChannel(channel);

            then(channelJpaRepository.findById(channelId))
                    .isPresent();
            then(channelRedisRepository.findById(channelId))
                    .isPresent();
        }

        @Test
        @DisplayName("업데이트 테스트")
        void test2() {
            var channelId = "happykoo";
            var channel = channelPersistenceAdapter.loadChannel(channelId).get();

            var newSnippet = new ChannelSnippetRequest("marco-channel", "마르코 채널", channel.getSnippet().getThumbnailUrl());
            channel.updateSnippet(newSnippet);

            channelPersistenceAdapter.saveChannel(channel);

            var result = channelPersistenceAdapter.loadChannel(channelId);

            assertEquals(newSnippet.title(), result.get().getSnippet().getTitle());
            assertEquals(newSnippet.description(), result.get().getSnippet().getDescription());
        }
    }
}
