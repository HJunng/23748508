package org.skmnservice.boardapp.board;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_type", nullable = false)
    private String fileType; // MIME 타입

    @Column(name = "file_size", nullable = false)
    private Long fileSize; // 파일 크기

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData; // 바이너리 데이터

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    public Attachment(Post post, String fileName, String filePath, String fileType, Long fileSize, byte[] fileData) {
         this.post = post;
         this.fileName = fileName;
         this.filePath = filePath;
         this.fileType = fileType;
         this.fileSize = fileSize;
         this.fileData = fileData;
    }
}
