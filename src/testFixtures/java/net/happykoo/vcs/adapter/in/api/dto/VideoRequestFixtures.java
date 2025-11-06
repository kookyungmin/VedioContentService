package net.happykoo.vcs.adapter.in.api.dto;

public class VideoRequestFixtures {
    public static VideoRequest stub() {
        return new VideoRequest(
                "title",
                "desc",
                "https://happykoo.net/image.jpg",
                "happykoo-channel"
        );
    }
}
