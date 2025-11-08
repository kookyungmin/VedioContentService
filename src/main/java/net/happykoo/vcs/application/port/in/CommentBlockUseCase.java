package net.happykoo.vcs.application.port.in;

import net.happykoo.vcs.domain.user.User;

public interface CommentBlockUseCase {
    void blockComment(User user, String commentId);
}
