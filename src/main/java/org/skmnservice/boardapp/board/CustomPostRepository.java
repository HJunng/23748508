package org.skmnservice.boardapp.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository{
    // 제목, 작성자ID로 검색
    Page<Post> findAllPostsByTitleAndByUsername(String keyword, Pageable pageable);
}
