package net.happykoo.vcs.adapter.in.api.dto;

import net.happykoo.vcs.domain.video.VideoRate;

public record VideoRateResponse(String videoId, VideoRate rate) {
}
