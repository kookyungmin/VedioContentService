package net.happykoo.vcs.application.port;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.in.api.dto.ChannelRequest;
import net.happykoo.vcs.application.port.in.ChannelUseCase;
import net.happykoo.vcs.application.port.out.LoadChannelPort;
import net.happykoo.vcs.application.port.out.SaveChannelPort;
import net.happykoo.vcs.domain.channel.Channel;
import net.happykoo.vcs.domain.channel.ChannelSnippet;
import net.happykoo.vcs.domain.channel.ChannelStatistics;
import net.happykoo.vcs.exception.DomainNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelService implements ChannelUseCase {
    private final SaveChannelPort saveChannelPort;
    private final LoadChannelPort loadChannelPort;

    @Override
    public Channel createChannel(ChannelRequest channelRequest) {
        var channel = Channel.builder()
                .id(UUID.randomUUID().toString())
                .snippet(
                        ChannelSnippet.builder()
                                .title(channelRequest.snippet().title())
                                .description(channelRequest.snippet().description())
                                .thumbnailUrl(channelRequest.snippet().thumbnailUrl())
                                .publishedAt(LocalDateTime.now())
                                .build()
                )
                .statistics(ChannelStatistics.getDefaultStatistics())
                .contentOwnerId(channelRequest.contentOwnerId())
                .build();

        saveChannelPort.saveChannel(channel);
        return channel;
    }

    @Override
    public Channel updateChannel(String channelId, ChannelRequest channelRequest) {
        var channel = getChannel(channelId);
        channel.updateSnippet(channelRequest.snippet());

        saveChannelPort.saveChannel(channel);

        return channel;
    }

    @Override
    public Channel getChannel(String channelId) {
        return loadChannelPort.loadChannel(channelId)
                .orElseThrow(DomainNotFoundException::new);
    }
}
