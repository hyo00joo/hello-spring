package hello.hello_spring.repository;

import hello.hello_spring.domain.PostImage;
import hello.hello_spring.domain.ProfileImage;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public class JpaImageRepository  {

    private final EntityManager em;

    public JpaImageRepository(EntityManager em) {this.em = em;}


    public void savePostImage(PostImage image) {
        em.persist(image);
    }

    public List<PostImage> findByPostId(Long postId) {
        List<PostImage> images =
        em.createQuery("select im from PostImage im where im.post.id = :postId", PostImage.class)
                .setParameter("postId", postId)
                .getResultList();
        return images;
    }

    public void saveProfileImage(ProfileImage profileImage) {
        em.persist(profileImage);
    }

    public void deleteProfileIMage(ProfileImage profileImage) {
        em.remove(profileImage);
    }

    public ProfileImage findProfileImageByUserId(Long userId) {
        return em.createQuery("select im from ProfileImage im where im.member.id = :userId", ProfileImage.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }
}
