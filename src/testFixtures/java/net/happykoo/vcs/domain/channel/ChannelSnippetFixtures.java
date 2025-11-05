package net.happykoo.vcs.domain.channel;

import java.time.LocalDateTime;

public class ChannelSnippetFixtures {
    public static ChannelSnippet stub() {
        return ChannelSnippet.builder()
                .title("happykoo-channel")
                .description("happykoo 채널")
                .thumbnailUrl("https://happykoo.net/thumbnail.jpg")
                .publishedAt(LocalDateTime.now())
                .build();
    }

    public static ChannelSnippet anotherStub() {
        return ChannelSnippet.builder()
                .title("marco-channel")
                .description("marco 채널")
                .thumbnailUrl("https://happykoo.net/thumbnail2.jpg")
                .publishedAt(LocalDateTime.now())
                .build();
    }
}
