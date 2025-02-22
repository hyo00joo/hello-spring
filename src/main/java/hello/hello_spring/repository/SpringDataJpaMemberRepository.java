//package hello.hello_spring.repository;
//
//import hello.hello_spring.domain.Member;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface SpringDataJpaMemberRepository extends JpaRepository<Member, Long>, MemberRepository {
////  JpaRepository를 받고 있으면 구현체를 자동으로 만들어주어서 스프링빈에 자동으로 등록해줌
//    @Override
//    Optional<Member> findByName(String name);
//    @Override
//    Optional<Member> findByPassword(String password);
//    @Override
//    int delete(String password, String name);
//    @Override
//    Long countMember();
//}
