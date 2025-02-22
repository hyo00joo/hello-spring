package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.domain.ProfileImage;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class JpaMemberRepository implements MemberRepository {


    //jpa는 entitymanager로 동작
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }


    @Override
    public Optional<Member> findByPassword(String password) {
        List<Member> result = em.createQuery("select m from Member m where m.password = :password", Member.class)
                .setParameter("password", password)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    @Override
    public int delete(String password, String name) {
        return em.createQuery("delete from Member m " +
                        "where m.password = :password and m.name = :name")
                .setParameter("password", password)
                .setParameter("name", name)
                .executeUpdate();
    }

    @Override
    public Long countMember() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    @Override
    public int changePassword(String name, String password, String newPassword) {
      return em.createQuery("update Member m set m.password = :newPassword " +
                              "where m.name = :name and m.password = :password")
              .setParameter("name", name)
              .setParameter("password", password)
              .setParameter("newPassword", newPassword)
              .executeUpdate();
    }


    @Override
    public int changeEmail(String name, String password, String newEmail) {
        return em.createQuery("update Member m set m.email = :newEmail" +
                        " where m.name = :name and m.password = :password")
                .setParameter("name", name)
                .setParameter("password", password)
                .setParameter("newEmail", newEmail)
                .executeUpdate();
    }

    @Override
    public Optional <Member> login(String name, String password) {
        List<Member> result = em.createQuery("select m from Member m " +
                        "where m.name = :name and m.password = :password", Member.class)
                .setParameter("name", name)
                .setParameter("password", password)
                .getResultList();
        return result.stream().findAny();
    }


    }




