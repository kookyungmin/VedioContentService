package net.happykoo.vcs.application.port.in;

import net.happykoo.vcs.adapter.in.api.dto.ChannelRequest;
import net.happykoo.vcs.domain.channel.Channel;

public interface ChannelUseCase {
    Channel createChannel(ChannelRequest channelRequest);
    Channel updateChannel(String channelId, ChannelRequest channelRequest);
    Channel getChannel(String channelId);
}
