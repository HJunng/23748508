package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.board.dto.PostDetailDto;
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

    // 게시글 상세 조회
    public PostDetailDto getPost(Long id) {
        // 조회수 증가
        incrementViewCount(id);

        Post post =  postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return PostDetailDto.of(post);
    }

    // 조회수 증가
    private void incrementViewCount(Long id) {
        postRepository.findById(id).ifPresent(post -> {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        });
    }

}