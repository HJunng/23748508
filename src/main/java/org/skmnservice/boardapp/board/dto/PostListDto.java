package org.skmnservice.boardapp.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.skmnservice.boardapp.board.Post;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PostListDto {
    private String title;
    private String username;
    private Long viewCount;
    private boolean hasAttachments;
    private LocalDateTime createdAt;

    public static PostListDto of(Post post) {
        return PostListDto.builder()
                .title(post.getTitle())
                .username(post.getUser().getUsername())
                .viewCount(post.getViewCount())
                .hasAttachments(!post.getAttachments().isEmpty())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
