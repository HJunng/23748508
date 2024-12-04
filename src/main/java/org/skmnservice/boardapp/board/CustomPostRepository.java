package org.skmnservice.boardapp.board;

import java.util.List;

public interface CustomPostRepository{
    // 제목, 작성자ID로 검색
    List<Post> findAllPostsByTitleAndByUsername(String keyword);
}
