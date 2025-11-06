package net.happykoo.vcs.adapter.out.jpa.subscribe;

import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.user.UserJpaEntity;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SubscribeJpaRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SubscribeJpaRepository subscribeJpaRepository;

    @BeforeEach
    void setUp() {
        //subscribe entity 저장
        var user = entityManager.persist(new UserJpaEntity("happykoo", "해피쿠", "https://example.com/profile.jpg"));
        for (int i = 0; i < 3; i++) {
            var channel = entityManager.persist(ChannelJpaEntity.from(ChannelFixtures.stub("channel" + i)));
            entityManager.persist(new SubscribeJpaEntity(UUID.randomUUID().toString(), channel, user));
        }
    }

    @Test
    @DisplayName("findAllByUserId 테스트")
    void testFindAllChannelByUserId() {
        var result = subscribeJpaRepository.findAllByUserId("happykoo");

        then(result)
            .hasSize(3)
            .extracting("user.id").containsOnly("happykoo");
    }
}
