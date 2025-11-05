package net.happykoo.vcs.domain.channel;

public class ChannelStatisticsFixtures {
    public static ChannelStatistics stub() {
        return ChannelStatisticsFixtures.stub(50, 1, 20);
    }

    public static ChannelStatistics stub(int videoCount, int subscriberCount, int commentCount) {
        return ChannelStatistics.builder()
                .videoCount(videoCount)
                .subscriberCount(subscriberCount)
                .commentCount(commentCount)
                .build();
    }
}
