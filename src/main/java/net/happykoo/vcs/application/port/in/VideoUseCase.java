package net.happykoo.vcs.application.port.in;

import net.happykoo.vcs.adapter.in.api.dto.VideoRequest;
import net.happykoo.vcs.domain.video.Video;

import java.util.List;

public interface VideoUseCase {
    Video getVideo(String videoId);
    List<Video> listVideos(String channelId);
    Video createVideo(VideoRequest videoRequest);
    void increaseViewCount(String videoId);
}
