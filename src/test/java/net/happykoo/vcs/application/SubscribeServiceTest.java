package net.happykoo.vcs.application;

import net.happykoo.vcs.application.port.out.LoadChannelPort;
import net.happykoo.vcs.application.port.out.LoadUserPort;
import net.happykoo.vcs.application.port.out.SubscribePort;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import net.happykoo.vcs.domain.user.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SubscribeServiceTest {
    private SubscribeService subscribeService;

    private SubscribePort subscribePort = mock(SubscribePort.class);
    private LoadChannelPort loadChannelPort = mock(LoadChannelPort.class);
    private LoadUserPort loadUserPort = mock(LoadUserPort.class);

    @BeforeEach
    void setUp() {
        subscribeService = new SubscribeService(loadUserPort, loadChannelPort, subscribePort);
    }

    @Test
    @DisplayName("subscribeChannel 테스트")
    void test1() {
        var subscribeId = UUID.randomUUID().toString();
        given(loadChannelPort.loadChannel(any())).willReturn(Optional.of(ChannelFixtures.stub("channelId")));
        given(loadUserPort.loadUser(any())).willReturn(Optional.of(UserFixtures.stub()));
        given(subscribePort.insertSubscribeChannel(any(), any())).willReturn(subscribeId);

        var result = subscribeService.subscribeChannel("channelId", "happykoo");

        then(result).isEqualTo(subscribeId);
    }

    @Test
    @DisplayName("listSubscribeChannel 테스트")
    void test2() {
        var list = IntStream.range(1, 4)
                .mapToObj(i -> ChannelFixtures.stub("channelId" + i))
                .toList();
        given(subscribePort.listSubscribeChannel(any())).willReturn(list);

        var result = subscribeService.listSubscribeChannel("happykoo");

        then(result).hasSize(3);
    }
}
