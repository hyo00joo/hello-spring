package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class JpaPostRepository implements PostRepository {


    private final EntityManager em;

    public JpaPostRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(Post post) {
        em.persist(post); // 데이터를 영속화(Persist)합니다.
    }

    @Override
    public void delete(Post post) {
        em.remove(em.merge(post));
    }


    @Override
    public Page<Post> findByUserId(Long userId, Pageable pageable, String title) {
        boolean hasTitle = (title != null && !title.trim().isEmpty());

        String queryString = "SELECT p FROM Post p WHERE p.author.id = :memberId";
        if (hasTitle) {
            queryString += " AND p.title like :title";
        }
        queryString += " ORDER BY p.createdAt DESC";
        TypedQuery<Post> query = em.createQuery(queryString, Post.class);

        query.setParameter("memberId", userId);

        if (hasTitle) {
            query.setParameter("title", "%" + title.trim() + "%");
        }

        query.setFirstResult((int) pageable.getOffset()); // OFFSET 적용
        query.setMaxResults(pageable.getPageSize()); // LIMIT 적용

        List<Post> posts = query.getResultList();

        String cntQuery = "SELECT COUNT(p) FROM Post p WHERE p.author.id = :memberId";
        if (hasTitle) {
            cntQuery += " AND p.title like :title";
        }
        TypedQuery<Long> countQuery = em.createQuery(cntQuery, Long.class);
        countQuery.setParameter("memberId", userId);

        if (hasTitle) {
            countQuery.setParameter("title", "%" + title.trim() + "%");
        }

        Long totalCount = countQuery.getSingleResult();
        return new PageImpl<>(posts, pageable, totalCount);
    }

    @Override
    public Post findByPostId(Long postId) {
        Post post = em.find(Post.class, postId);
        return post;
    }

//    @Override
//    public List<Post> findByTitleContaining(String title) {
//        return em.createQuery("select p from Post p where p.title like %:title%", Post.class)
//                .setParameter("title", title)
//                .getResultList();
//    }

    @Override
    public Page<Post> findAllMembersPosts(Pageable pageable, String title) {
        boolean hasTitle = (title != null && !title.trim().isEmpty());

        String queryString = "SELECT p FROM Post p";
        if (hasTitle) {
            queryString += " WHERE p.title like :title";
        }
        queryString += " ORDER BY p.createdAt DESC";
        TypedQuery<Post> query = em.createQuery(queryString, Post.class);

        if (hasTitle) {
            query.setParameter("title", "%" + title.trim() + "%");
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Post> posts = query.getResultList();

        String cntQuery = "SELECT COUNT(p) FROM Post p";
        if (hasTitle) {
            cntQuery += " WHERE p.title like :title";
        }
        TypedQuery<Long> countQuery = em.createQuery(cntQuery, Long.class);
        if(hasTitle) {
            countQuery.setParameter("title", "%" + title.trim() + "%");
        }

        Long totalCount = countQuery.getSingleResult();

       return new PageImpl<>(posts, pageable, totalCount);
    }



}






