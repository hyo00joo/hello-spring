package hello.hello_spring.service;

import hello.hello_spring.domain.Post;
import hello.hello_spring.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public void deletePost(Post post) {postRepository.delete(post);}

//    public List<Post> findPostByUserId(Long userId) {
//        return postRepository.findByUserId(userId);
//    }
    public Page<Post> findPostByUserId(Long userId, Pageable pageable, String title) {return postRepository.findByUserId(userId, pageable, title);}

    public Post findPostByPostId(Long postId) {
        return postRepository.findByPostId(postId);
    }
//    public List<Post> findByTitleContaining(String title) {
//        return postRepository.findByTitleContaining(title);
//    }

    public Page<Post> findAllMembersPosts(Pageable pageable, String title) {
        return postRepository.findAllMembersPosts(pageable, title);}

}
