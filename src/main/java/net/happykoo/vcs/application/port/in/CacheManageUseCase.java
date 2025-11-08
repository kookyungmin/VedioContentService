package net.happykoo.vcs.application.port.in;

import java.util.List;

public interface CacheManageUseCase {
    List<String> getAllCacheNames();
    boolean deleteCache(String cacheKey);
}
