package net.happykoo.vcs.adapter.out.redis.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.happykoo.vcs.domain.channel.Channel;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static net.happykoo.vcs.common.CacheNames.CHANNEL;

@RedisHash(value = CHANNEL) //key space 명
@AllArgsConstructor
@Getter
public class ChannelRedisHash {
    @Id //redis key
    private String id; //channel:88xxx-xxxxxxxxx
    private ChannelSnippetRedisHash snippet;
    private ChannelStatisticsRedisHash statistics;
    @Indexed //redis에 별도의 키를 생성해주고, 조회가능하게 해줌
    private String contentOwnerId; //channel:contentOwnerId:user (channel:88xxx-xxxxxxxxx 등 channel id들이 멤버로 저장)

    public static ChannelRedisHash from(Channel channel) {
        return new ChannelRedisHash(channel.getId(),
                ChannelSnippetRedisHash.from(channel.getSnippet()),
                ChannelStatisticsRedisHash.from(channel.getStatistics()),
                channel.getContentOwnerId());
    }

    public Channel toDomain() {
        return Channel.builder()
                .id(this.id)
                .snippet(this.snippet.toDomain())
                .statistics(this.statistics.toDomain())
                .contentOwnerId(this.contentOwnerId)
                .build();
    }
}
