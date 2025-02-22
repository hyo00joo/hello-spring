package hello.hello_spring.controller;


import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.domain.ProfileImage;
import hello.hello_spring.repository.JpaImageRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PostRepository;
import hello.hello_spring.security.CookieUtil;
import hello.hello_spring.security.JwtUtil;
import hello.hello_spring.service.MemberService;
import hello.hello_spring.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//component scan

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PostService postService;
    private final PostRepository postRepository;
    private final JpaImageRepository jpaImageRepository;
    JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    public MemberController(MemberService memberService,
                            MemberRepository memberRepository,
                            PostService postService, PostRepository postRepository, JpaImageRepository jpaImageRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.jpaImageRepository = jpaImageRepository;
    }

    @GetMapping("/new") //데이터를 조회할때
    public String creatForm() {
        return "members/createMember"; //template에서 찾음 (createMemberForm.html)
    }


    @GetMapping("/list")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        model.addAttribute("count", memberRepository.countMember());

        //addAtribute 는 html에서 members를 "members"라는 이름으로 접근가능하게함
        return "members/memberList";
    }


    @GetMapping("/delete")
    public String deleteForm(HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }

        return "members/deleteMember";
    }


    @GetMapping("/update")
    public String updateOpt(HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }
        return "members/updateMember";
    }


    @GetMapping("/updatePassword")
    public String updatePasswordForm(HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }
        return "members/updatePassword";
    }


    @GetMapping("/updateEmail")
    public String updateEmailForm(HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }

        return "members/updateEmail";
    }


    @GetMapping("/login")
    public String loginForm() {
        return "members/loginMember";
    }

    @GetMapping("/myProfile")
    public String profile(HttpServletRequest request, Model model) {
        // 세션에서 로그인 사용자 정보 가져오기

        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/";
        }
        Optional<Member> loginUser = memberRepository.findByName(username);
        // 로그인한 사용자 정보 모델에 추가

        ProfileImage image = jpaImageRepository.findProfileImageByUserId(loginUser.get().getId());
        if (image.getImageFile() != null) {
            model.addAttribute("delete", true);
            model.addAttribute("image", image.getImageFile());
        } else {
            model.addAttribute("image", "https://hyoeun-image.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2025-02-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+3.30.05.png");
            model.addAttribute("delete", false);
        }



        return "/members/myProfile";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }

        CookieUtil.clearCookie(response, "jwt");
        return "redirect:/";
    }


    @GetMapping("/home")
    public String memberHome(HttpServletRequest request, Model model) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }

        return "members/home";
    }


    // 글 작성 화면 제공
    @GetMapping("/newPost")
    public String memberDashboard(HttpServletRequest request, Model model) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }

        return "members/newPost"; // 대시보드 템플릿
    }


    @GetMapping("/board/postContent/{postId}")
    public String getPostDetail(@PathVariable("postId") Long postId, HttpServletRequest request, Model model) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }
        Post post = postService.findPostByPostId(postId); // Post를 ID로 조회
        List<String> images = new ArrayList<>();

        jpaImageRepository.findByPostId(postId).forEach(image -> {
//           Base64.getEncoder().encodeToString(image);
            images.add(image.getImageFile());
        });
        model.addAttribute("images", images);
        model.addAttribute("post", post);
        model.addAttribute("deleteEnabled", false);
        return "members/postDetail"; // 상세보기 페이지 반환
    }

    @GetMapping("/myPosts/postContent/{postId}")
    public String getMyPostDetail(@PathVariable("postId") Long postId, HttpServletRequest request, Model model) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }
        Post post = postService.findPostByPostId(postId); // Post를 ID로 조회
        model.addAttribute("post", post);
        model.addAttribute("deleteEnabled", true);
        return "members/postDetail"; // 상세보기 페이지 반환
    }

    @GetMapping("/profile/{id}")
    public String profile(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);
        if (username == null) {
            return "redirect:/";
        }
        ProfileImage image = jpaImageRepository.findProfileImageByUserId(id);
        if (image.getImageFile() == null) {
            model.addAttribute("profileImage", "https://hyoeun-image.s3.ap-northeast-2.amazonaws.com/%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA+2025-02-12+%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE+3.30.05.png");
        } else {
            model.addAttribute("profileImage", image.getImageFile());
        }

        Optional<Member> user = memberRepository.findById(id);
        model.addAttribute("user", user.get());
        return "members/profile";
    }

}


