package net.happykoo.vcs.adapter.in.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.happykoo.vcs.adapter.in.api.dto.VideoRequest;
import net.happykoo.vcs.application.port.in.VideoUseCase;
import net.happykoo.vcs.domain.video.VideoFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoApiController.class)
public class VideoApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VideoUseCase videoUseCase;

    @Nested
    @DisplayName("GET /api/v1/videos/{videoId} 테스트")
    class GetVideoTest {
        @Test
        @DisplayName("video 가 있으면 200 OK")
        void test1() throws Exception {
            // Given
            var videoId = "videoId";
            given(videoUseCase.getVideo(any())).willReturn(VideoFixtures.stub(videoId));

            // When
            mockMvc
                .perform(
                    get("/api/v1/videos/{videoId}", videoId)
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.id").value(videoId)
                );
        }
    }

    @Nested
    @DisplayName("GET /api/v1/videos?channelId={channelId} 테스트")
    class ListVideoTest {
        @Test
        @DisplayName("200 OK, 목록 반환")
        void test1() throws Exception {
            // Given
            var channelId = "happykoo-channel";
            var list = LongStream.range(1L, 4L)
                    .mapToObj(i -> VideoFixtures.stub("videoId" + i))
                    .toList();
            given(videoUseCase.listVideos(any())).willReturn(list);

            // When
            mockMvc
                .perform(
                    get("/api/v1/videos?channelId={channelId}", channelId)
                )
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$.data.size()").value(3),
                    jsonPath("$.data[0].channelId").value(channelId)
                );
        }
    }

    @Nested
    @DisplayName("POST /api/v1/videos 테스트")
    class CreateVideo {
        @Test
        @DisplayName("200 OK, 생성된 Video id 를 반환")
        void test1() throws Exception {
            // given
            var videoId = "videoId";
            var channelId = "channelId";
            var videoRequest = new VideoRequest("title", "desc", "https://happykoo.net/image.jpg", channelId);
            given(videoUseCase.createVideo(any())).willReturn(VideoFixtures.stub(videoId));

            // when
            mockMvc
                .perform(
                    post("/api/v1/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(videoRequest))
                )
                .andExpectAll(
                    status().isOk()
                );

            // then
            verify(videoUseCase).createVideo(any());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/videos/view")
    class VideoViewCountTest {
        @Test
        @DisplayName("200 OK, 조회수 증가")
        void testViewCount() throws Exception {
            // Given
            var videoId = "videoId";
            willDoNothing().given(videoUseCase).increaseViewCount(any());

            // When
            mockMvc
                .perform(
                    post("/api/v1/videos/{videoId}/view", videoId)
                )
                .andExpectAll(
                    status().isOk()
                );

            // Then
            verify(videoUseCase).increaseViewCount(videoId);
        }
    }

}
