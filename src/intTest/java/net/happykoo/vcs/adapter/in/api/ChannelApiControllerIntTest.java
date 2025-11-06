package net.happykoo.vcs.adapter.in.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.happykoo.vcs.adapter.out.jpa.channel.ChannelJpaRepository;
import net.happykoo.vcs.config.TestRedisConfig;
import net.happykoo.vcs.domain.channel.ChannelFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestRedisConfig.class)
@AutoConfigureMockMvc
class ChannelApiControllerIntTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChannelJpaRepository channelJpaRepository;

    @Test
    @DisplayName("POST /api/v1/channels")
    void test1() throws Exception {
        var body = ChannelFixtures.stub("test");

        mockMvc
            .perform(post("/api/v1/channels")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(body))
            )
            .andExpect(status().isOk());

        then(channelJpaRepository.count())
                .isEqualTo(1);
    }

}
