package net.happykoo.vcs.adapter.out;

import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.subscribe.SubscribeJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.subscribe.SubscribeJpaRepository;
import net.happykoo.vcs.adapter.out.jpa.user.UserJpaEntity;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import net.happykoo.vcs.domain.user.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class SubscribePersistenceAdapterTest {
    private SubscribePersistenceAdapter subscribePersistenceAdapter;

    private SubscribeJpaRepository subscribeJpaRepository = mock(SubscribeJpaRepository.class);
    private StringRedisTemplate stringRedisTemplate = mock(StringRedisTemplate.class);
    private SetOperations<String, String> setOperations = mock(SetOperations.class);

    @BeforeEach
    void setUp() {
        subscribePersistenceAdapter = new SubscribePersistenceAdapter(subscribeJpaRepository, stringRedisTemplate);
        given(stringRedisTemplate.opsForSet()).willReturn(setOperations);
    }

    @Test
    @DisplayName("insertSubscribeChannel 테스트")
    void test1() {
        var channel = ChannelFixtures.stub("channelId");
        var user = UserFixtures.stub();
        given(subscribeJpaRepository.save(any()))
                .willAnswer(arg -> arg.getArgument(0));

        var result = subscribePersistenceAdapter.insertSubscribeChannel(channel, user);

        then(result).isNotNull();
        verify(subscribeJpaRepository).save(any());
        verify(setOperations, times(2)).add(any(), any());
    }

    @Test
    @DisplayName("deleteSubscribeChannel 테스트")
    void test2() {
        var subscribeId = "subscribedId";
        var channel = ChannelFixtures.stub("channelId");
        var user = UserFixtures.stub();
        given(subscribeJpaRepository.findByChannelIdAndUserId(any(), any()))
                .willReturn(Optional.of(new SubscribeJpaEntity(subscribeId, ChannelJpaEntity.from(channel), UserJpaEntity.from(user))));

        subscribePersistenceAdapter.deleteSubscribeChannel(channel, user);
        verify(subscribeJpaRepository).deleteById(any());
        verify(setOperations, times(2)).remove(any(), any());
    }

    @Test
    @DisplayName("listSubscribeChannel 테스트")
    void test3() {
        var list = IntStream.range(1, 4)
            .mapToObj(i ->
                new SubscribeJpaEntity(
                    String.valueOf(i),
                    ChannelJpaEntity.from(ChannelFixtures.stub(String.valueOf(i))),
                    UserJpaEntity.from(UserFixtures.stub())
                )
            )
            .toList();

        given(subscribeJpaRepository.findAllByUserId(any()))
                .willReturn(list);

        var result = subscribePersistenceAdapter.listSubscribeChannel("userId");

        then(result).hasSize(3);
    }
}
