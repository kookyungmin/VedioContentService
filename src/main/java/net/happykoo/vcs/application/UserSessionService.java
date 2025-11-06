package net.happykoo.vcs.application;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.in.UserSessionUseCase;
import net.happykoo.vcs.application.port.out.UserSessionPort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSessionService implements UserSessionUseCase {
    private final UserSessionPort userSessionPort;

    @Override
    public String getUserId(String authKey) {
        return userSessionPort.getUserId(authKey);
    }
}
