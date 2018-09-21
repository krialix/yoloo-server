package com.yoloo.server.feed.repository;

import com.yoloo.server.feed.jpa.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
