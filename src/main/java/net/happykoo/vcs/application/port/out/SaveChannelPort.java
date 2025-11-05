package net.happykoo.vcs.application.port.out;

import net.happykoo.vcs.domain.channel.Channel;

public interface SaveChannelPort {
    void saveChannel(Channel channel);
}
