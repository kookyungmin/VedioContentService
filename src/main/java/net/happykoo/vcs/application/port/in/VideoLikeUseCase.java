package net.happykoo.vcs.application.port.in;

public interface VideoLikeUseCase {
    Long likeVideo(String videoId, String userId);
    Long unlikeVideo(String videoId, String userId);
    Boolean isLikedVideo(String videoId, String userId);
    Long getVideoLikeCount(String videoId);
}
