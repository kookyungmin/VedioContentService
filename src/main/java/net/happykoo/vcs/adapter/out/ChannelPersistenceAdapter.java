package net.happykoo.vcs.adapter.out;

import lombok.RequiredArgsConstructor;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaRepository;
import net.happykoo.vcs.adapter.out.redis.channel.ChannelRedisHash;
import net.happykoo.vcs.adapter.out.redis.channel.ChannelRedisRepository;
import net.happykoo.vcs.application.port.out.LoadChannelPort;
import net.happykoo.vcs.application.port.out.SaveChannelPort;
import net.happykoo.vcs.domain.channel.Channel;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChannelPersistenceAdapter implements SaveChannelPort, LoadChannelPort {
    private final ChannelJpaRepository channelJpaRepository;
    private final ChannelRedisRepository channelRedisRepository;

    @Override
    public void saveChannel(Channel channel) {
        //write-through 방식 redis update, jpa update
        channelRedisRepository.save(ChannelRedisHash.from(channel));
        channelJpaRepository.save(ChannelJpaEntity.from(channel));
    }

    @Override
    public Optional<Channel> loadChannel(String id) {
        return channelRedisRepository.findById(id)
                //redis hit
                .map(ChannelRedisHash::toDomain)
                .or(() -> {
                    //redis cache miss
                    var channelOpt = channelJpaRepository.findById(id)
                                    .map(ChannelJpaEntity::toDomain);
                    //redis에 저장
                    channelOpt.ifPresent((channel) -> channelRedisRepository.save(ChannelRedisHash.from(channel)));
                    return channelOpt;
                });
    }
}
