package net.happykoo.vcs.application;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.in.CacheManageUseCase;
import net.happykoo.vcs.application.port.out.CacheManagePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheManageService implements CacheManageUseCase {
    private final CacheManagePort cacheManagePort;

    @Override
    public List<String> getAllCacheNames() {
        return cacheManagePort.getAllCacheNames();
    }

    @Override
    public boolean deleteCache(String cacheKey) {
        return cacheManagePort.deleteCache(cacheKey);
    }
}
