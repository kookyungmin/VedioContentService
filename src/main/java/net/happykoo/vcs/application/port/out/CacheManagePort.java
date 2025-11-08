package net.happykoo.vcs.application.port.out;

import java.util.List;

public interface CacheManagePort {
    List<String> getAllCacheNames();
    List<String> getAllCacheNames(String pattern);
    boolean deleteCache(String cacheKey);
}
