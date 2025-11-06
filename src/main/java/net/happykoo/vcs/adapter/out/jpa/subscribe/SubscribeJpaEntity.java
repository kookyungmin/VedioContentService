package net.happykoo.vcs.adapter.out.jpa.subscribe;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaEntity;
import net.happykoo.vcs.adapter.out.jpa.user.UserJpaEntity;

@Entity(name = "subscribe")
@Table(name = "vcs_subscribe")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SubscribeJpaEntity {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private ChannelJpaEntity channel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpaEntity user;
}
