package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.PostRepository;
import hello.hello_spring.security.CookieUtil;
import hello.hello_spring.service.MemberService;
import hello.hello_spring.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import hello.hello_spring.security.CookieUtil;
import hello.hello_spring.security.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
@RequestMapping("/members")
public class searchController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PostService postService;
    private final PostRepository postRepository;
    JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    public searchController(MemberRepository memberRepository, MemberService memberService, PostService postService, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @GetMapping("/searchMyPosts")
    public String myPostsFindByTitle(
//            @PathVariable(name = "currentPage", d) Integer page, //Integer이기 때문에 required = false 시 에러발생
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "currentPage", defaultValue = "0") Integer page,
//            @RequestParam(name ="size", defaultValue = "5") Integer size,
            HttpServletRequest request, Model model) {

        String token = CookieUtil.getTokenFromCookie(request);
        if (token == null) {
            return "redirect:/";
        }
        String username = jwtUtil.validateToken(token);
        Optional<Member> loginUser = memberRepository.findByName(username);
        Long userId = loginUser.get().getId();

        Pageable pageable = PageRequest.of(page, 5);
        Page<Post> userPosts = postService.findPostByUserId(userId, pageable, title);

        // 날짜 포맷 처리
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일");
        List<Map<String, Object>> postsWithDates = new ArrayList<>();
        userPosts.toList().forEach(post -> {
            Map<String, Object> postWithDate = new HashMap<>();
            postWithDate.put("post", post);
            postWithDate.put("userName", post.getAuthor().getName());
            postWithDate.put("formattedDate", post.getCreatedAt().format(formatter));
            postsWithDates.add(postWithDate);
        });

        // 모델에 데이터 추가
        model.addAttribute("targetTitle", title);
        model.addAttribute("userPosts", postsWithDates);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPosts.getTotalPages());

        return "/members/myPosts";
    }

    @GetMapping("/searchBoard")
    public String BoardFindByTitle(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "currentPage", defaultValue = "0") Integer page,
            HttpServletRequest request, Model model) {
        String token = CookieUtil.getTokenFromCookie(request);

        if (token == null) {
            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
        }
        Pageable pageable = PageRequest.of(page, 5);
        Page<Post> allPosts = postService.findAllMembersPosts(pageable, title);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일");
        List<Map<String, Object>> postsWithDates = new ArrayList<>();

        allPosts.forEach(post -> {
            Map<String, Object> postWithDate = new HashMap<>();
            if(post.getAuthor() == null) {

                    Member deletedMember = new Member();
                    deletedMember.setName("알수 없음");
                    memberRepository.save(deletedMember);

                post.setAuthor(deletedMember);
            }
            postWithDate.put("post", post);
            postWithDate.put("formattedDate", post.getCreatedAt().format(formatter));
            postsWithDates.add(postWithDate);
        });

        model.addAttribute("targetTitle", title);
        model.addAttribute("allPosts", postsWithDates);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", allPosts.getTotalPages());
        return "/members/board"; // 검색 결과 페이지로 이동
    }

}
