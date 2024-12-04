package org.skmnservice.boardapp.board.repository;

import org.skmnservice.boardapp.board.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository{
    // 제목, 작성자ID로 검색
    Page<Post> findAllPostsByTitleAndByUsername(String keyword, Pageable pageable);
}
