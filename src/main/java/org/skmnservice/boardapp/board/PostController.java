package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import org.skmnservice.boardapp.board.dto.PostListDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
        List<PostListDto> posts = postService.searchPosts(keyword);
        model.addAttribute("postList", posts);
        return "board/posts";
    }
}
