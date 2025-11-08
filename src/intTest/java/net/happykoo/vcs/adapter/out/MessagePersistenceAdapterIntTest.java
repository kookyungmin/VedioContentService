package net.happykoo.vcs.adapter.out;

import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaRepository;
import net.happykoo.vcs.adapter.out.jpa.subscribe.SubscribeJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.subscribe.SubscribeJpaRepository;
import net.happykoo.vcs.adapter.out.jpa.user.UserJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.user.UserJpaRepository;
import net.happykoo.vcs.config.TestRedisConfig;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import net.happykoo.vcs.domain.user.UserFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(classes = TestRedisConfig.class)
public class MessagePersistenceAdapterIntTest {
    @Autowired
    private MessagePersistenceAdapter messagePersistenceAdapter;
    @Autowired
    private ChannelJpaRepository channelJpaRepository;
    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private SubscribeJpaRepository subscribeJpaRepository;

    @BeforeEach
    public void setUp() {
        // 여러 개의 구독 정보 생성
        var channelJpaEntity = channelJpaRepository.save(ChannelJpaEntity.from(ChannelFixtures.stub("happykoo-channel")));
        for (int i = 0; i < 5; i++) {
            var userJpaEntity = userJpaRepository.save(UserJpaEntity.from(UserFixtures.stub(UUID.randomUUID().toString())));
            var subscribeJpaEntity = new SubscribeJpaEntity(
                    UUID.randomUUID().toString(),
                    channelJpaEntity,
                    userJpaEntity
            );
            subscribeJpaRepository.save(subscribeJpaEntity);
        }
    }

    @Test
    @DisplayName("메세지 전송 테스트")
    public void test1() {
        messagePersistenceAdapter.sendNewVideMessage("happykoo-channel", "videoId");
    }
}
