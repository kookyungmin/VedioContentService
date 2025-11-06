package net.happykoo.vcs.adapter.in.api;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.in.api.dto.Response;
import net.happykoo.vcs.adapter.in.api.dto.VideoRateResponse;
import net.happykoo.vcs.adapter.in.resolver.LoginUser;
import net.happykoo.vcs.application.port.in.VideoLikeUseCase;
import net.happykoo.vcs.domain.user.User;
import net.happykoo.vcs.domain.video.VideoRate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/videos/rate")
@RequiredArgsConstructor
public class VideoRateApiController {
    private final VideoLikeUseCase videoLikeUseCase;

    @PostMapping
    public Response<Void> rateVideo(@LoginUser User user, @RequestParam String videoId, @RequestParam VideoRate rating) {
        switch (rating) {
            case LIKE:
                videoLikeUseCase.likeVideo(videoId, user.getId());
                break;
            case NONE:
                videoLikeUseCase.unlikeVideo(videoId, user.getId());
                break;
        }
        return Response.ok();
    }

    @GetMapping
    public Response<VideoRateResponse> getRate(@LoginUser User user, @RequestParam String videoId) {
        var rate = videoLikeUseCase.isLikedVideo(videoId, user.getId()) ? VideoRate.LIKE : VideoRate.NONE;
        return Response.ok(new VideoRateResponse(videoId, rate));
    }
}
