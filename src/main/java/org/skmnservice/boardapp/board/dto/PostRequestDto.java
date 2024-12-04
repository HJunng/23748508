package org.skmnservice.boardapp.board.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostRequestDto {
    private String title;
    private String content;
    private List<MultipartFile> attachments;
}
