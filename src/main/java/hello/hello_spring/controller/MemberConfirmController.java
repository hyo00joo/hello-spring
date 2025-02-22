//package hello.hello_spring.controller;
//
//import hello.hello_spring.domain.Member;
//import hello.hello_spring.repository.MemberRepository;
//import hello.hello_spring.service.MemberService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//// JSON 응답을 위한 DTO 클래스
//
//
//@RestController
//@RequestMapping("/members")
//public class MemberConfirmController {
//
//    private final MemberService memberService;
//    private final MemberRepository memberRepository;
//
//    public MemberConfirmController(MemberRepository memberRepository
//            , MemberService memberService, MemberService memberService1) {
//        this.memberService = memberService1;
//        this.memberRepository = memberRepository;
//    }
//
//    @PostMapping("/new")
//    public Map<String, Object> confirm(@RequestBody MemberForm form) {
//        Member member = new Member();
//        member.setName(form.getName());
//        member.setEmail(form.getEmail());
//        member.setPassword(form.getPassword());
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("member", member);
//        if( memberService.join(member) == null){
//            result.put("type", "member");
//            result.put("message", "중복된 회원입니다");
//        }
//        if(form.getName().isEmpty()) {
//            result.put("type", "name");
//            result.put("message", "이름을 다시 입력해주세요.");
//        }
//        if(form.getEmail().isEmpty()) {
//            result.put("type", "email");
//            result.put("message", "이메일을 다시 입력해주세요.");
//        }
//        if(form.getPassword().isEmpty()) {
//            result.put("type", "password");
//            result.put("message", "비밀번호를 다시 입력해주세요.");
//        }
//
//
//        return  result;
//    }
//
//}
