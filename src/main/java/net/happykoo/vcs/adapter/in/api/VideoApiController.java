package net.happykoo.vcs.adapter.in.api;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.in.api.dto.Response;
import net.happykoo.vcs.adapter.in.api.dto.VideoRequest;
import net.happykoo.vcs.application.port.in.VideoUseCase;
import net.happykoo.vcs.domain.video.Video;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoApiController {
    private final VideoUseCase videoUseCase;

    @GetMapping("{videoId}")
    public Response<Video> getVideo(@PathVariable String videoId) {
        return Response.ok(videoUseCase.getVideo(videoId));
    }

    @GetMapping(params = "channelId")
    public List<Video> listVideo(@RequestParam String channelId) {
        return videoUseCase.listVideos(channelId);
    }

    @PostMapping
    public Response<String> createVideo(@RequestBody VideoRequest videoRequest) {
        var video = videoUseCase.createVideo(videoRequest);
        return Response.ok(video.getId());
    }

    @PostMapping("{videoId}/view")
    public Response<Void> increaseVideoViewCount(@PathVariable String videoId) {
        videoUseCase.increaseViewCount(videoId);
        return Response.ok();
    }
}
