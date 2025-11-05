package net.happykoo.vcs.adapter.out.redis.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.happykoo.vcs.domain.channel.ChannelStatistics;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "channelStatistics")
@AllArgsConstructor
@Getter
public class ChannelStatisticsRedisHash {
    private long videoCount;
    private long subscriberCount;
    private long commentCount;

    public static ChannelStatisticsRedisHash from(ChannelStatistics channelStatistics) {
        return new ChannelStatisticsRedisHash(channelStatistics.getVideoCount(), channelStatistics.getSubscriberCount(), channelStatistics.getCommentCount());
    }

    public ChannelStatistics toDomain() {
        return ChannelStatistics.builder()
                .commentCount(this.commentCount)
                .subscriberCount(this.subscriberCount)
                .videoCount(this.videoCount)
                .build();
    }
}
