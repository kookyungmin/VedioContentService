package net.happykoo.vcs.common;

import static net.happykoo.vcs.common.CacheNames.SEPARATOR;
import static net.happykoo.vcs.common.CacheNames.VIDEO_VIEW_COUNT;

public class RedisKeyGenerator {
    public static String getVideoViewCountKey(String videoId) {
        return VIDEO_VIEW_COUNT + SEPARATOR +  videoId;
    }
}
