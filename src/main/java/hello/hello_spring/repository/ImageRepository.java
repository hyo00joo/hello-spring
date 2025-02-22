package hello.hello_spring.repository;

import hello.hello_spring.domain.PostImage;
import hello.hello_spring.domain.ProfileImage;

import java.util.List;

public interface ImageRepository {
    public void save(PostImage image);
    public List<PostImage> findByPstId(Long postId);
    public void saveProfileImage(ProfileImage image);
}
