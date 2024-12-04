package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        Attachment attachment = attachmentService.getFile(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(attachment.getFileType()));
        headers.setContentDisposition(ContentDisposition.inline().filename(attachment.getFileName()).build());

        return new ResponseEntity<>(attachment.getFileData(), headers, HttpStatus.OK);
    }

    // 기존 첨부파일 삭제
    @DeleteMapping("/{attachmentId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteAttachment(
            @PathVariable Long attachmentId
    ) {
        log.info("Deleting attachment: {}", attachmentId);
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.ok().build();
    }
}
