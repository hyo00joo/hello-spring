package hello.hello_spring.controller;


import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//component scan
@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new") //데이터를 조회할때
    public String creatForm() {
        return "members/createMemberForm"; //template에서 찾음 (createMemberForm.html)
    }

    //@Get에서 들어가고 post 방식이니까 @Post되는 듯
    @PostMapping("/members/new") //데이터를 등록할때
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());
        if( memberService.join(member) == null || form.getName().trim().isEmpty()) {
            return "members/memberFailed";
        }

        member.setName(form.getName());

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        //addAtribute 는 html에서 members를 "members"라는 이름으로 접근가능하게함
        return "members/memberList";
    }


    @GetMapping("members/delete")
    public String deleteForm() {
        return "members/deleteMember";

    }


    @PostMapping("members/delete")
    public String deleteMember(Member member) {
//        System.out.println(member.getGender());
        if(!memberService.deleteMember(member)) {
            return "members/deleteMemberFailed";
        }
        return "redirect:/";
    }

}
