package net.happykoo.vcs.application;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.in.CommentBlockUseCase;
import net.happykoo.vcs.application.port.out.BlockCommentPort;
import net.happykoo.vcs.domain.user.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentBlockService implements CommentBlockUseCase {
    private final BlockCommentPort blockCommentPort;

    @Override
    public void blockComment(User user, String commentId) {
        blockCommentPort.saveUserCommentBlock(user.getId(), commentId);
    }
}
