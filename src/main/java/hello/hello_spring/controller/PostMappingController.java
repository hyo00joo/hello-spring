package hello.hello_spring.controller;


import hello.hello_spring.domain.PostImage;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/members")
public class PostMappingController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PostService postService;
    private final PostRepository postRepository;
    private final JpaImageRepository jpaImageRepository;

    public PostMappingController(MemberService memberService,
                                 MemberRepository memberRepository, PostService postService, PostRepository postRepository, JpaImageRepository jpaImageRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.postService = postService;
        this.postRepository = postRepository;
        this.jpaImageRepository = jpaImageRepository;
    }


    private final JwtUtil jwtUtil = new JwtUtil();


    @PostMapping("/new")
    public ResponseEntity<Map<String, String>> data(@RequestBody MemberForm memberForm) {

        Map<String, String> msg = new HashMap<>();
        Boolean isValidName = !(memberForm.getName() == null || memberForm.getName().trim().isEmpty());
        Boolean isValidEmail = validateEmail(memberForm.getEmail());
        Boolean isEmptyEmail = (memberForm.getEmail() == null || memberForm.getEmail().trim().isEmpty());
        Boolean isValidPassword = !(memberForm.getPassword() == null || memberForm.getPassword().trim().isEmpty());
        Boolean isSamePassword = memberForm.getPassword().equals(memberForm.getPasswordConfirm());

        if (isValidName && isValidEmail && !isEmptyEmail && isValidPassword && isSamePassword) {

            Member member = new Member();
            member.setName(memberForm.getName());
            if (memberService.validateDuplicatedMember(member.getName())) {
                msg.put("name", "중복된 이름입니다.");
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }

            member.setEmail(memberForm.getEmail());
            member.setPassword(memberForm.getPassword());
            ProfileImage profileImage = new ProfileImage();
            memberRepository.save(member);
            profileImage.setMember(member);
            jpaImageRepository.saveProfileImage(profileImage);


            return new ResponseEntity<>(msg, HttpStatus.OK);

        }
        if (!isValidName) {
            msg.put("name", "이름을 입력해 주세요.");
        }
        if (isEmptyEmail) {
            msg.put("email", "이메일을 입력해주세요.");
        } else if (!isValidEmail) {
            msg.put("email", "옳바르지 않는 이메일입니다.");
        }
        if (!isValidPassword) {
            msg.put("password", "비밀번호를 입력해주세요.");
        }
        if (!isSamePassword) {
            msg.put("passwordConfirm", "비밀번호가 일치하지 않습니다.");
        }

        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }


    @PostMapping("/delete")
    public ResponseEntity<Map<String, String>> delete(@RequestBody MemberForm memberForm, HttpServletRequest request) {
        Map<String, String> msg = new HashMap<>();
        Boolean isValidName = !(memberForm.getName() == null || memberForm.getName().trim().isEmpty());
        Boolean isValidPassword = !(memberForm.getPassword() == null || memberForm.getPassword().trim().isEmpty());

        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);

        if (username == null) {
            msg.put("error", "인증되지 않은 사용자입니다.");
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }

        Optional<Member> loginUser = memberRepository.findByName(username);

        if (!isValidName) {
            msg.put("name", "이름을 입력해 주세요.");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        if (!isValidPassword) {
            msg.put("password", "비밀번호를 입력해주세요.");
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }

        if (isValidName && isValidPassword) {
            if ((!Objects.equals(memberForm.getName(), loginUser.get().getName()))
                    || !Objects.equals(memberForm.getPassword(), loginUser.get().getPassword())) {
                msg.put("name", "이름 또는 비밀번호를 다시 확인해주세요.");

                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
            else {
                memberService.deleteMember(memberForm);

            }
        }

        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/updateEmail")
    public ResponseEntity<Map<String, String>> updateEmail(@RequestBody MemberForm memberForm, HttpServletRequest request) {
        Map<String, String> msg = new HashMap<>();
        Boolean isValidName = !(memberForm.getName() == null || memberForm.getName().trim().isEmpty());
        Boolean isValidEmail = validateEmail(memberForm.getEmail());
        Boolean isEmptyEmail = (memberForm.getEmail() == null || memberForm.getEmail().trim().isEmpty());
        Boolean isValidPassword = !(memberForm.getPassword() == null || memberForm.getPassword().trim().isEmpty());


        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);
        if (username == null) {
            msg.put("error", "인증되지 않은 사용자입니다.");
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }
        Optional<Member> loginUser = memberRepository.findByName(username);
        if (isValidName && isValidEmail && !isEmptyEmail && isValidPassword) {
            if ((!Objects.equals(memberForm.getName(), loginUser.get().getName()))
                    || !Objects.equals(memberForm.getPassword(), loginUser.get().getPassword())) {
                msg.put("name", "회원정보가 일치하지 않습니다.");
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
            if (!(memberService.updateEmail(memberForm.getName(), memberForm.getPassword()
                    , memberForm.getEmail()))) {

                msg.put("name", "회원정보가 일치하지 않습니다.");
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        if (!isValidName) {
            msg.put("name", "이름을 입력해 주세요.");
        }
        if (isEmptyEmail) {
            msg.put("email", "이메일을 입력해주세요.");
        } else if (!isValidEmail) {
            msg.put("email", "옳바르지 않는 이메일입니다.");
        }
        if (!isValidPassword) {
            msg.put("password", "비밀번호를 입력해주세요.");
        }


        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody MemberForm memberForm,
                                                              HttpServletRequest request) {


        Map<String, String> msg = new HashMap<>();
        Boolean isValidName = !(memberForm.getName() == null || memberForm.getName().trim().isEmpty());
        Boolean isValidPassword = !(memberForm.getPassword() == null || memberForm.getPassword().trim().isEmpty());
        Boolean isValidNewPassword = !(memberForm.getNewPassword() == null || memberForm.getNewPassword().trim().isEmpty());


        String token = CookieUtil.getTokenFromCookie(request);
        String username = jwtUtil.validateToken(token);
        if (username == null) {
            msg.put("error", "인증되지 않은 사용자입니다.");
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }

        Optional<Member> loginUser = memberRepository.findByName(username);
        if (isValidName && isValidPassword && isValidNewPassword) {
            if ((!Objects.equals(memberForm.getName(), loginUser.get().getName()))
                    || !Objects.equals(memberForm.getPassword(), loginUser.get().getPassword())) {
                msg.put("name", "회원정보가 일치하지 않습니다.");
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
            if (!(memberService.updatePassword(memberForm.getName(),
                    memberForm.getPassword(), memberForm.getNewPassword()))) {
                msg.put("name", "회원정보가 일치하지 않습니다.");
                return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }

        if (!isValidName) {
            msg.put("name", "이름을 입력해 주세요.");
        }
        if (!isValidPassword) {
            msg.put("password", "비밀번호를 입력해주세요.");
        }
        if (!isValidPassword) {
            msg.put("newPassword", "새 비밀번호를 입력해주세요.");
        }


        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);

    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return Pattern.matches(emailRegex, email);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody MemberForm memberForm,
                                                     HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> msg = new HashMap<>();
        Boolean isValidName = !(memberForm.getName() == null || memberForm.getName().trim().isEmpty());
        Boolean isValidPassword = !(memberForm.getPassword() == null || memberForm.getPassword().trim().isEmpty());

        Optional<Member> member = memberService.login(memberForm);

        if (isValidName && isValidPassword) {
            if (member.isPresent()) {

                String token = jwtUtil.generateToken(member.get().getName());
                CookieUtil.addTokenToCookie(response, token);
                msg.put("message", "환영합니다!" + member.get().getName() + "님!");
                return new ResponseEntity<>(msg, HttpStatus.OK);
            }
            // 로그인 실패: 잘못된 이름 또는 비밀번호
            return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
        }
        if (!isValidName) {
            msg.put("name", "이름을 입력해 주세요.");
        }
        if (!isValidPassword) {
            msg.put("password", "비밀번호를 입력해주세요.");
        }

        return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
    }


    @PostMapping("/newPost")
    public ResponseEntity<Map<String, String>> newPost(@RequestParam("title") String title,
                                                       @RequestParam("content") String content,
                                                       @RequestParam("is_private") Boolean isPrivate,
                                                       @RequestParam(value = "images", required = false) List<MultipartFile> images,
                                                       HttpServletRequest request) throws IOException {
        Map<String, String> msg = new HashMap<>();
        String token = CookieUtil.getTokenFromCookie(request);
        if (token == null) {
            return new ResponseEntity<>(msg, HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtil.validateToken(token);
        Optional<Member> loginUser = memberRepository.findByName(username);

        Boolean isValidTitle = !(title == null || title.trim().isEmpty());
        Boolean isValidContent = !(content == null || content.trim().isEmpty());

        if (isValidTitle && isValidContent) {
            Post post = new Post();
            post.setAuthor(loginUser.get());
            post.setTitle(title);
            post.setContent(content);
            post.setIs_private(isPrivate);
            postService.createPost(post);

            if (images != null) {// MultipartFile을 담을 리스트 생성

                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {

                        byte[] imageBytes = image.getBytes();
                        // 이미지 파일을 Base64로 인코딩
                        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

                        // Image 객체에 Base64 인코딩된 이미지 저장
                        PostImage imageBean = new PostImage();
                        imageBean.setImageFile(imageBase64);
                        imageBean.setPost(post);
                        jpaImageRepository.savePostImage(imageBean);  // 변환된 List<MultipartFile>을 setImages에 전달
                    }
                }
            }


            return new ResponseEntity<>(HttpStatus.OK);
        }

        if (!isValidTitle) {
            msg.put("title", "제목을 입력해 주세요.");
        }
        if (!isValidContent) {
            msg.put("content", "내용을 입력해주세요.");
        }
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/deletePost")
    public ResponseEntity<?> deletePost(@RequestBody Post post, HttpServletRequest request) {

        String token = CookieUtil.getTokenFromCookie(request);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Post postTarget = postService.findPostByPostId(post.getPostId());
        postService.deletePost(postTarget);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}






