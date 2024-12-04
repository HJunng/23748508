package org.skmnservice.boardapp.board;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.board.repository.AttachmentRepository;
import org.skmnservice.boardapp.board.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final PostRepository postRepository;

    public Attachment getFile(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다."));
    }

    // 새 첨부파일 추가
    public void saveAttachments(Post post, List<MultipartFile> attachments) {
        // 첨부파일 저장
        for (MultipartFile file : attachments) {
            try {
                Attachment attachment = Attachment.builder()
                        .post(post)
                        .fileName(file.getOriginalFilename())
                        .filePath("/uploads/" + file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .fileData(file.getBytes())
                        .build();

                checkFile(file);

                attachmentRepository.save(attachment);
            } catch(IOException e) {
                throw new RuntimeException("파일 저장 중 오류 발생: " + file.getOriginalFilename(), e);
            }
        }
    }

    private void checkFile(MultipartFile file) {
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB 제한
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }

        String[] allowedExtensions = {".jpg", ".png", ".pdf"};
        if (Arrays.stream(allowedExtensions).noneMatch(file.getOriginalFilename()::endsWith)) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }
    }

    // 기존 첨부파일 삭제
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAttachment(Long attachmentId) {
        // 첨부파일 확인 및 삭제
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new IllegalArgumentException("첨부파일이 존재하지 않습니다."));

        // 데이터베이스에서 첨부파일 삭제
        attachmentRepository.delete(attachment);
    }
}
