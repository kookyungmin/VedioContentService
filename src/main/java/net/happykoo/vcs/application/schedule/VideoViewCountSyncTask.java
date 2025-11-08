package net.happykoo.vcs.application.schedule;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.application.port.out.CacheManagePort;
import net.happykoo.vcs.application.port.out.LoadVideoPort;
import net.happykoo.vcs.application.port.out.SaveVideoPort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VideoViewCountSyncTask {
    private final CacheManagePort cacheManagePort;
    private final LoadVideoPort loadVideoPort;
    private final SaveVideoPort saveVideoPort;

    @Scheduled(fixedRate = 60000)
    public void syncVideoViewCount() {
        // schedule 동작 확인용
        System.out.println(LocalDateTime.now());

//        방법 1, 전체 키를 전부 불러오는 경우 -> 비효율적
//        cacheManagePort.getAllCacheNames(VIDEO_VIEW_COUNT + SEPARATOR)
//            .forEach(key -> {
//                var videoId = key.replace(VIDEO_VIEW_COUNT + SEPARATOR, "");
//                System.out.println("sync:" + videoId);
//
//                saveVideoPort.syncViewCount(videoId);
//            });

        // 방법 2, viewCount 변경이 있는 video 목록만 조회
        loadVideoPort.getAllVideoIdsWithViewCount()
                .forEach(saveVideoPort::syncViewCount);
    }
}
