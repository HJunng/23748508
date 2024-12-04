package org.skmnservice.boardapp.board.repository;

import org.skmnservice.boardapp.board.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

}
