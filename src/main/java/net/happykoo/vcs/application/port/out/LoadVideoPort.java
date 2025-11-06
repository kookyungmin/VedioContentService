package net.happykoo.vcs.application.port.out;

import net.happykoo.vcs.domain.video.Video;

import java.util.List;

public interface LoadVideoPort {
    Video loadVideo(String videoId);
    List<Video> loadVideoByChannel(String channelId);
    Long getViewCount(String videoId);
}
