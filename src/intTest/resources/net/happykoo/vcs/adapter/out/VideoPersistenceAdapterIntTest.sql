INSERT INTO vcs_channel (id, title, description, published_at, thumbnail_url, subscriber_count, video_count, comment_count, content_owner_id)
VALUES ('happykoo-channel', '해피쿠 채널', '해피쿠 채널', '2025-11-06 00:00:00', 'https://happykoo.net', 100, 100, 100, 'happykoo');

INSERT INTO vcs_video (id, title, description, channel_id, thumbnail_url, file_url, published_at, view_count, like_count)
VALUES ('video1', 'video1', 'video1', 'happykoo', 'https://happykoo.net', 'https://happykoo.net/video1.mp4', '2025-11-06 00:00:00', 0, 0);
INSERT INTO vcs_video (id, title, description, channel_id, thumbnail_url, file_url, published_at, view_count, like_count)
VALUES ('video2', 'video2', 'video2', 'happykoo', 'https://happykoo.net', 'https://happykoo.net/video2.mp4', '2025-11-06 00:00:00', 0, 0);