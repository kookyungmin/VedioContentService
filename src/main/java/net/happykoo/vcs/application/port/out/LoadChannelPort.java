package net.happykoo.vcs.application.port.out;

import net.happykoo.vcs.domain.channel.Channel;

import java.util.Optional;

public interface LoadChannelPort {
    Optional<Channel> loadChannel(String id);
}
