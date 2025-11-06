package net.happykoo.vcs.application;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.in.SubscribeUseCase;
import net.happykoo.vcs.application.port.out.LoadChannelPort;
import net.happykoo.vcs.application.port.out.LoadUserPort;
import net.happykoo.vcs.application.port.out.SubscribePort;
import net.happykoo.vcs.domain.channel.Channel;
import net.happykoo.vcs.exception.DomainNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService implements SubscribeUseCase {
    private final LoadUserPort loadUserPort;
    private final LoadChannelPort loadChannelPort;
    private final SubscribePort subscribePort;

    @Override
    public String subscribeChannel(String channelId, String userId) {
        var channel = loadChannelPort.loadChannel(channelId).orElseThrow(DomainNotFoundException::new);
        var user = loadUserPort.loadUser(userId).orElseThrow(DomainNotFoundException::new);

        return subscribePort.insertSubscribeChannel(channel, user);
    }

    @Override
    public void unsubscribeChannel(String channelId, String userId) {
        var channel = loadChannelPort.loadChannel(channelId).orElseThrow(DomainNotFoundException::new);
        var user = loadUserPort.loadUser(userId).orElseThrow(DomainNotFoundException::new);

        subscribePort.deleteSubscribeChannel(channel, user);
    }

    @Override
    public List<Channel> listSubscribeChannel(String userId) {
        return subscribePort.listSubscribeChannel(userId);
    }
}
