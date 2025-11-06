package net.happykoo.vcs.application.port.out;

import net.happykoo.vcs.domain.user.User;

import java.util.Optional;

public interface LoadUserPort {
    Optional<User> loadUser(String userId);
}
