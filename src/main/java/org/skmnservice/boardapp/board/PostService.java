package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import org.skmnservice.boardapp.board.dto.PostListDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    /**
     * 전체 게시판 목록
     * todo: 페이징 처리 구현
     */
    public List<PostListDto> searchPosts(String keyword) {
        List<Post> posts = postRepository.findAllPostsByTitleAndByUsername(keyword);
        return posts.stream().
                map(PostListDto::of).
                toList();
    }
}