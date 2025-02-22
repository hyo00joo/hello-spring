package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface PostRepository {
    void save(Post post);
    void delete(Post post);
//    List<Post> findByUserId(Long userId);
    Page<Post> findByUserId(Long userId, Pageable pageable, String title);
    Post findByPostId(Long postId);
//    List<Post> findByTitleContaining(String title);
    Page<Post> findAllMembersPosts(Pageable pageable, String title);


}
