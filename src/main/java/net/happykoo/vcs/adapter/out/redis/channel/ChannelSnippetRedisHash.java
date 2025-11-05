package net.happykoo.vcs.adapter.out.redis.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelSnippetJpaEntity;
import net.happykoo.vcs.domain.channel.ChannelSnippet;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("channelSnippet")
@AllArgsConstructor
@Getter
public class ChannelSnippetRedisHash {
    private String title;
    private String description;
    private String thumbnailUrl;
    private LocalDateTime publishedAt;

    public static ChannelSnippetRedisHash from(ChannelSnippet channelSnippet) {
        return new ChannelSnippetRedisHash(channelSnippet.getTitle(), channelSnippet.getDescription(), channelSnippet.getThumbnailUrl(), channelSnippet.getPublishedAt());
    }

    public ChannelSnippet toDomain() {
        return ChannelSnippet.builder()
                .title(this.title)
                .description(this.description)
                .thumbnailUrl(this.thumbnailUrl)
                .publishedAt(this.publishedAt)
                .build();
    }
}
