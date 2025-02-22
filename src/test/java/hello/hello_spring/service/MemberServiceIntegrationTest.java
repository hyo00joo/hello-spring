//package hello.hello_spring.service;
//
//import hello.hello_spring.domain.Member;
//import hello.hello_spring.repository.MemberRepository;
//import hello.hello_spring.repository.MemoryMemberRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//
//@SpringBootTest
////테스트가 끝나면 디비에 넣어놨던 데이터를 되돌림.
//@Transactional
//class MemberServiceIntegrationTest {
//
//    //spring container에서 member service, member repo 꺼내옴
//    @Autowired MemberService memberService;
//    @Autowired MemberRepository memberRepository;
//
//
//    @Test
//    void 회원가입() {
//        //given
//        Member member = new Member();
//        member.setName("spring");
//
//        //when
//        Long saveId = memberService.join(member);
//
//        //then
//        Member findMember = memberService.findOne(saveId).get();
//        Assertions.assertThat(member.getName()).isEqualTo(findMember.getName());
//    }
//
//    @Test
//    public void 중복_회원_예외() {
//        //given
//        Member member1 = new Member();
//        member1.setName("spring");
//
//        Member member2 = new Member();
//        member2.setName("spring");
//
//        //when
//        memberService.join(member1);
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));
//
//        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//
//    }
//
//}