package org.skmnservice.boardapp.board;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skmnservice.boardapp.board.dto.PostDetailDto;
import org.skmnservice.boardapp.board.dto.PostListDto;
import org.skmnservice.boardapp.board.dto.PostRequestDto;
import org.skmnservice.boardapp.board.repository.PostRepository;
import org.skmnservice.boardapp.user.User;
import org.skmnservice.boardapp.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;

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

    /**
     * 글 작성
     */
    @Transactional // 하나의 트랜잭션으로 실행
    public void createPost(String username, PostRequestDto dto) {
        log.info("글 작성자 : "+username);

        try {
            // 사용자 정보 확인
            User user = userRepository.findUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

            // 게시글 생성
            Post post = Post.builder()
                    .user(user)
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();

            postRepository.save(post);

            // 첨부파일이 있는 경우만 저장
            List<MultipartFile> attachments = dto.getAttachments();
            if (attachments != null && !attachments.isEmpty()) {
                attachmentService.saveAttachments(post, attachments);
            }

        } catch (Exception e) {
            throw new RuntimeException("게시글 저장 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 글 수정
     */
    @Transactional
    public void modifyPost(String username, Long postId, PostRequestDto dto) {
        //게시글 검색
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 작성자 검증
        if(!validateAuthor(post, username)) {
            throw new AccessDeniedException("작성자만 접근할 수 있습니다.");
        }
        log.info("게시글 수정 : "+postId);
        
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        // 첨부파일이 있는 경우만 저장
        List<MultipartFile> attachments = dto.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            attachmentService.saveAttachments(post, attachments);
        }

        postRepository.save(post);
    }

    public void deletePost(String username, Long postId) {
        //게시글 검색
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 작성자 검증
        if(!validateAuthor(post, username)) {
            throw new AccessDeniedException("작성자만 접근할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    private boolean validateAuthor(Post post, String username) {
        return post.getUser().getUsername().equals(username);
    }

}