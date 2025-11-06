package net.happykoo.vcs.common;

import static net.happykoo.vcs.common.CacheNames.*;

public class RedisKeyGenerator {
    public static String getVideoViewCountKey(String videoId) {
        return VIDEO_VIEW_COUNT + SEPARATOR +  videoId;
    }

    public static String getUserSessionKey(String authKey) {
        return USER_SESSION + SEPARATOR + authKey;
    }
    public static String getVideoLikeKey(String videoId) {
        return VIDEO_LIKE + SEPARATOR +  videoId;
    }
}
