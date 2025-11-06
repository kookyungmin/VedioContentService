package net.happykoo.vcs.application.port.out;

import net.happykoo.vcs.domain.video.Video;

public interface SaveVideoPort {
    void saveVideo(Video video);
    void incrementViewCount(String videoId);
}
