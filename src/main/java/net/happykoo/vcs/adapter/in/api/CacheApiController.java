package net.happykoo.vcs.adapter.in.api;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.in.api.dto.Response;
import net.happykoo.vcs.application.port.in.CacheManageUseCase;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/caches")
@RequiredArgsConstructor
public class CacheApiController {
    private final CacheManageUseCase cacheManageUseCase;

    @GetMapping("cache-names")
    public Response<List<String>> cacheNames() {
        return Response.ok(cacheManageUseCase.getAllCacheNames());
    }

    @DeleteMapping(params = "cacheKey")
    public Response<Void> deleteCache(@RequestParam String cacheKey) {
        cacheManageUseCase.deleteCache(cacheKey);
        return Response.ok();
    }
}
