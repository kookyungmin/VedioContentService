package net.happykoo.vcs.domain.video;

public class VideoFixtures {
    public static Video stub(String id) {
        return Video.builder()
                .id(id)
                .title("video title")
                .description("video description")
                .thumbnailUrl("https://happykoo.net/thumbnail.jpg")
                .fileUrl("https://happykoo.net/video.mp4")
                .channelId("happykoo-channel")
                .viewCount(0L)
                .build();
    }
}
