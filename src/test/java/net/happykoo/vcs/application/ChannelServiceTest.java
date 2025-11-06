package net.happykoo.vcs.application;

import net.happykoo.vcs.adapter.in.api.dto.ChannelRequest;
import net.happykoo.vcs.adapter.in.api.dto.ChannelSnippetRequest;
import net.happykoo.vcs.application.port.out.LoadChannelPort;
import net.happykoo.vcs.application.port.out.SaveChannelPort;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;

class ChannelServiceTest {
    private ChannelService channelService;

    private final LoadChannelPort loadChannelPort = mock(LoadChannelPort.class);
    private final SaveChannelPort saveChannelPort = mock(SaveChannelPort.class);

    @BeforeEach
    void setUp() {
        channelService = new ChannelService(saveChannelPort, loadChannelPort);
    }

    @Test
    @DisplayName("createChannel 테스트")
    void testCreateChannel() {
        // Given
        var channelRequest = new ChannelRequest(new ChannelSnippetRequest("happykoo-channel", "happykoo 채널", "https://happykoo.net/thumbnail.jpg"), "happykoo");
        willDoNothing().given(saveChannelPort).saveChannel(any());
        // When
        var result = channelService.createChannel(channelRequest);
        // Then
        then(result)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("snippet.title", channelRequest.snippet().title())
                .hasFieldOrPropertyWithValue("snippet.description", channelRequest.snippet().description())
                .hasFieldOrPropertyWithValue("snippet.thumbnailUrl", channelRequest.snippet().thumbnailUrl())
                .hasFieldOrPropertyWithValue("statistics.videoCount", 0L)
                .hasFieldOrPropertyWithValue("statistics.commentCount", 0L)
                .hasFieldOrPropertyWithValue("statistics.subscriberCount", 0L)
                .hasFieldOrPropertyWithValue("contentOwnerId", channelRequest.contentOwnerId());
    }

    @Test
    @DisplayName("updateChannel")
    void testUpdateChannel() {
        // Given
        var channelId = "happykoo";
        var channelRequest = new ChannelRequest(new ChannelSnippetRequest("marco-channel", "marco 채널", "https://happykoo.net/thumbnail2.jpg"), "happykoo");
        given(loadChannelPort.loadChannel(any())).willReturn(Optional.of(ChannelFixtures.stub(channelId)));
        willDoNothing().given(saveChannelPort).saveChannel(any());
        // When
        var result = channelService.updateChannel(channelId, channelRequest);
        // Then
        then(result)
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("snippet.title", channelRequest.snippet().title())
                .hasFieldOrPropertyWithValue("snippet.description", channelRequest.snippet().description())
                .hasFieldOrPropertyWithValue("snippet.thumbnailUrl", channelRequest.snippet().thumbnailUrl())
                .hasFieldOrPropertyWithValue("contentOwnerId", channelRequest.contentOwnerId());
    }

    @Test
    @DisplayName("getChannel")
    void testGetChannel() {
        var id = "happykoo";
        given(loadChannelPort.loadChannel(any()))
                .willReturn(Optional.of(ChannelFixtures.stub(id)));

        var result = channelService.getChannel(id);

        then(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", id);
    }
}
