package net.happykoo.vcs.adapter.out.jpa.video;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoJpaRepository extends CrudRepository<VideoJpaEntity, String> {
    List<VideoJpaEntity> findByChannelId(String channelId);
}
