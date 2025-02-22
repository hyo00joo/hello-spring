package hello.hello_spring;

//import hello.hello_spring.repository.JdbcMemberRepository;
import hello.hello_spring.repository.JpaMemberRepository;
import hello.hello_spring.repository.JpaPostRepository;
import hello.hello_spring.repository.MemberRepository;
//import hello.hello_spring.repository.MemoryMemberRepository;
import hello.hello_spring.repository.PostRepository;
import hello.hello_spring.service.MemberService;
import hello.hello_spring.service.PostService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

//    private final MemberRepository memberRepository;
////
////
////     생성자가 하나인 경우에는 생략가능
//    @Autowired
//    public SpringConfig(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    private EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }



    @Bean
    public MemberRepository memberRepository() {
//        return new MemoryMemberRepository();
//        return new JdbcMemberRepository(dataSource);
        return new JpaMemberRepository(em);
    }

    @Bean
    public PostRepository postRepository() {
        return new JpaPostRepository(em);
    }
    @Bean
    public PostService postService() {
        return new PostService(postRepository());
    }


//    private DataSource dataSource; //스프링 부트가 데이터 소스를 만들어줌
//
//    @Autowired
//    public SpringConfig(DataSource dataSource) {
//        this.dataSource = dataSource;
//
//    }
//
}



