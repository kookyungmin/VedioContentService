package net.happykoo.vcs.adapter.in.api;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.in.resolver.LoginUser;
import net.happykoo.vcs.application.port.in.CommentBlockUseCase;
import net.happykoo.vcs.domain.user.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment/block")
@RequiredArgsConstructor
public class CommentBlockApiController {
    private final CommentBlockUseCase commentBlockUseCase;

    @PostMapping(params = "commentId")
    void blockComment(@LoginUser User user, @RequestParam String commentId) {
        commentBlockUseCase.blockComment(user, commentId);
    }
}
