package net.happykoo.vcs.adapter.out.jpa.channel;

import java.time.LocalDateTime;

public class ChannelJpaEntityFixtures {
    public static ChannelJpaEntity stub(String id) {
        return new ChannelJpaEntity(
                id,
                new ChannelSnippetJpaEntity("happykoo 채널", "해피쿠 채널", "https://happykoo.net/thumbnail", LocalDateTime.now()),
                new ChannelStatisticsJpaEntity(10L, 10L, 10L),
                "happykoo"
        );
    }
}
