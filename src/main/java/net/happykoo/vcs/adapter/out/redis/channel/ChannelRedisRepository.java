package net.happykoo.vcs.adapter.out.redis.channel;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChannelRedisRepository extends CrudRepository<ChannelRedisHash, String> {
    Optional<ChannelRedisHash> findByContentOwnerId(String contentOwnerId);
}
