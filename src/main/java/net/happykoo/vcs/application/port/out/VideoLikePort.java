package net.happykoo.vcs.application.port.out;

public interface VideoLikePort {
    Long addVideoLike(String videoId, String userId);

    Long removeVideoLike(String videoId, String userId);

    Boolean isVideoLikeMember(String videoId, String userId);

    Long getVideoLikeCount(String videoId);
}
