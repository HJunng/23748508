package org.skmnservice.boardapp.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findAllPostsByTitleAndByUsername(String keyword) {
        QPost post = QPost.post;

        return queryFactory
                .selectFrom(post)
                .where(
                        keyword != null ?
                                post.title.startsWith(keyword)
                                        .or(post.user.username.startsWith(keyword)) // OR 조건 추가
                                : null
                )
                .orderBy(post.createdAt.desc()) // createdAt 기준 내림차순 정렬
                .fetch();
    }
}
