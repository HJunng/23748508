package org.skmnservice.boardapp.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> findAllPostsByTitleAndByUsername(String keyword, Pageable pageable) {
        QPost post = QPost.post;

        List<Post> posts =  queryFactory
                .selectFrom(post)
                .where(
                        keyword != null ?
                                post.title.startsWith(keyword)
                                        .or(post.user.username.startsWith(keyword)) // OR 조건 추가
                                : null
                )
                .orderBy(post.createdAt.desc()) // createdAt 기준 내림차순 정렬
                .offset(pageable.getOffset()) // 페이지의 시작점
                .limit(pageable.getPageSize()) // 한 페이지에 표시할 데이터 수
                .fetch();

        // 전체 데이터 개수 조회
        long total = queryFactory
                .selectFrom(post)
                .where(
                        keyword != null ?
                                post.title.startsWith(keyword)
                                        .or(post.user.username.startsWith(keyword)) // OR 조건 추가
                                : null
                )
                .fetchCount();

        log.info("Total posts: {}", total);

        return new PageImpl<>(posts, pageable, total);
    }
}
