package net.happykoo.vcs.application.port.in;

import net.happykoo.vcs.domain.user.User;

public interface UserUseCase {
    User getUser(String userId);
}
