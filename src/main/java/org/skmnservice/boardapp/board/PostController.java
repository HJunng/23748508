package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.board.dto.PostListDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 목록 조회
    @GetMapping("/posts")
    public String getAllPosts(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0") int page,
        Model model
    ) {
        Page<PostListDto> posts = postService.searchPosts(keyword, page);
        List<PostListDto> postList = (posts != null) ? posts.getContent() : List.of();


        model.addAttribute("postList", postList);
        model.addAttribute("totalPages", (posts != null && posts.getTotalPages() > 0) ? posts.getTotalPages() : 1);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", (keyword != null) ? keyword : "");

        return "board/posts";
    }
}
