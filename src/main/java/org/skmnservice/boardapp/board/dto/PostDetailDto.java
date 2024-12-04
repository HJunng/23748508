package org.skmnservice.boardapp.board.dto;

import lombok.Builder;
import lombok.Data;
import org.skmnservice.boardapp.board.Attachment;
import org.skmnservice.boardapp.board.Post;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostDetailDto {
    private Long id;
    private String title;
    private String content;
    private List<Attachment> attachments;
    private String username;
    private Long viewCount;
    private LocalDateTime createdAt;

    public static PostDetailDto of(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .attachments(post.getAttachments())
                .username(post.getUser().getUsername())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
