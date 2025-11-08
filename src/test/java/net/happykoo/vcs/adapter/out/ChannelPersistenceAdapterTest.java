package net.happykoo.vcs.adapter.out;

import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaRepository;
import net.happykoo.vcs.adapter.out.redis.channel.ChannelRedisHash;
import net.happykoo.vcs.adapter.out.redis.channel.ChannelRedisRepository;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ChannelPersistenceAdapterTest {
    private ChannelPersistenceAdapter channelPersistenceAdapter;
    private final ChannelJpaRepository channelJpaRepository = mock(ChannelJpaRepository.class);
    private final ChannelRedisRepository channelRedisRepository = mock(ChannelRedisRepository.class);

    @BeforeEach
    void setUp() {
        channelPersistenceAdapter = new ChannelPersistenceAdapter(channelJpaRepository, channelRedisRepository);
    }

    @Nested
    @DisplayName("saveChannel 테스트")
    class SaveChannel {
        @Test
        @DisplayName("Channel 저장")
        void test1() {
            //given
            var channelId = "happykoo";
            var channel = ChannelFixtures.stub(channelId);

            //when
            channelPersistenceAdapter.saveChannel(channel);

            //then
            var jpaCaptor = ArgumentCaptor.forClass(ChannelJpaEntity.class);
            verify(channelJpaRepository).save(jpaCaptor.capture());
            then(jpaCaptor.getValue())
                    .hasFieldOrPropertyWithValue("id", channel.getId())
                    .hasFieldOrPropertyWithValue("snippet.title", channel.getSnippet().getTitle());

            var redisCaptor = ArgumentCaptor.forClass(ChannelRedisHash.class);
            verify(channelRedisRepository).save(redisCaptor.capture());
            then(redisCaptor.getValue())
                    .hasFieldOrPropertyWithValue("id", channel.getId())
                    .hasFieldOrPropertyWithValue("snippet.title", channel.getSnippet().getTitle());
        }
    }

    @Nested
    @DisplayName("loadChannel 테스트")
    class LoadChannel {
        @Test
        @DisplayName("Redis 에서 찾을 수 있으면 Redis에 있는 값 반환")
        void test1() {
            //given
            var channelId = "happykoo";
            given(channelRedisRepository.findById(any()))
                    .willReturn(Optional.of(ChannelRedisHash.from(ChannelFixtures.stub(channelId))));

            //when
            var result = channelPersistenceAdapter.loadChannel(channelId);

            //then
            verify(channelRedisRepository).findById(channelId);
            verify(channelJpaRepository, never()).findById(channelId);
            then(result)
                    .isPresent()
                    .hasValueSatisfying(channel -> then(channel)
                            .hasFieldOrPropertyWithValue("id", channelId));
        }

        @Test
        @DisplayName("Redis 에서 찾을 수 없으면 JPA에 있는 값 반환")
        void test2() {
            //given
            var channelId = "happykoo";
            given(channelRedisRepository.findById(any()))
                    .willReturn(Optional.empty());
            given(channelJpaRepository.findById(any()))
                    .willReturn(Optional.of(ChannelJpaEntity.from(ChannelFixtures.stub(channelId))));

            //when
            var result = channelPersistenceAdapter.loadChannel(channelId);

            //then
            verify(channelRedisRepository).findById(channelId);
            verify(channelJpaRepository).findById(channelId);
            then(result)
                    .isPresent()
                    .hasValueSatisfying(channel -> then(channel)
                            .hasFieldOrPropertyWithValue("id", channelId));

        }

        @Test
        @DisplayName("Redis, JPA 에서 찾을 수 없으면 Optional.empty 반환")
        void test3() {
            //given
            var channelId = "happykoo";
            given(channelRedisRepository.findById(any()))
                    .willReturn(Optional.empty());
            given(channelJpaRepository.findById(any()))
                    .willReturn(Optional.empty());

            //when
            var result = channelPersistenceAdapter.loadChannel(channelId);

            //then
            verify(channelRedisRepository).findById(channelId);
            verify(channelJpaRepository).findById(channelId);
            then(result)
                .isNotPresent();

        }
    }
}
