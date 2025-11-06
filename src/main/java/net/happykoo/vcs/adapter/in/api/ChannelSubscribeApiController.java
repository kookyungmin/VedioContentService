package net.happykoo.vcs.adapter.in.api;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.in.api.dto.Response;
import net.happykoo.vcs.adapter.in.resolver.LoginUser;
import net.happykoo.vcs.application.port.in.SubscribeUseCase;
import net.happykoo.vcs.domain.channel.Channel;
import net.happykoo.vcs.domain.user.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscribe")
@RequiredArgsConstructor
public class ChannelSubscribeApiController {
    private final SubscribeUseCase subscribeUseCase;

    @PostMapping
    public Response<String> subscribe(@LoginUser User user, @RequestParam String channelId) {
        return Response.ok(subscribeUseCase.subscribeChannel(channelId, user.getId()));
    }

    @DeleteMapping
    public Response<Void> unsubscribe(@LoginUser User user, @RequestParam String channelId) {
        subscribeUseCase.unsubscribeChannel(channelId, user.getId());
        return Response.ok();
    }

    @GetMapping("/mine")
    public Response<List<Channel>> listSubscribeChannelByUser(@LoginUser User user) {
        return Response.ok(subscribeUseCase.listSubscribeChannel(user.getId()));
    }
}
