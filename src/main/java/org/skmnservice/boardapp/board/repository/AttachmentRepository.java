package org.skmnservice.boardapp.board.repository;

import org.skmnservice.boardapp.board.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
