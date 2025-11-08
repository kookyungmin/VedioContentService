package net.happykoo.vcs.application.port.out;

import java.util.Set;

public interface BlockCommentPort {
    void saveUserCommentBlock(String userId, String commentId);
    Set<String> getUserCommentBlocks(String userId);
}
