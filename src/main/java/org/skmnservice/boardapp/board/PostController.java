package org.skmnservice.boardapp.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.board.dto.PostDetailDto;
import org.skmnservice.boardapp.board.dto.PostListDto;
import org.skmnservice.boardapp.board.dto.PostRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        log.info("totalPages : "+posts.getTotalPages());
        List<PostListDto> postList = (posts != null) ? posts.getContent() : List.of();


        model.addAttribute("postList", postList);
        model.addAttribute("totalPages", (posts != null && posts.getTotalPages() > 0) ? posts.getTotalPages() : 1);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", (keyword != null) ? keyword : "");

        return "board/posts";
    }

    /**
     * 게시글 1건 상세 조회
     * todo : Attachment 보여주는 페이지 구성.
     */
    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        log.info("post id : "+id);

        // 게시글 정보 조회
        PostDetailDto post = postService.getPost(id);

        model.addAttribute("post", post);
        return "board/post-detail"; // 상세보기 페이지로 이동
    }

    /**
     * 게시글 작성
     */
    @GetMapping("/posts/write")
    public String writePostPage(Model model) {
        return "board/post-write";
    }

    @PostMapping("/posts/write")
    public String writePost(
            @ModelAttribute PostRequestDto postRequestDto,
            Principal principal
            ) {

        postService.createPost(principal.getName(), postRequestDto);
        return "redirect:/api/posts";
    }
}
