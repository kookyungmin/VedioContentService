package net.happykoo.vcs.application.port.in;

import net.happykoo.vcs.domain.channel.Channel;

import java.util.List;

public interface SubscribeUseCase {
    String subscribeChannel(String channelId, String userId);
    void unsubscribeChannel(String channelId, String userId);
    List<Channel> listSubscribeChannel(String userId);
}
