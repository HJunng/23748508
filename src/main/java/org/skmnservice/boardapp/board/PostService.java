package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.board.dto.PostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 전체 게시판 목록
     * todo: 페이징 처리 구현
     */
    public Page<PostListDto> searchPosts(String keyword, int page) {
        log.info(keyword+" "+page);
        Pageable pageable = PageRequest.of(page, 10);
        return postRepository.findAllPostsByTitleAndByUsername(keyword, pageable)
                .map(PostListDto::of);
    }
}