package hello.hello_spring.controller;


import hello.hello_spring.repository.JpaImageRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Transactional
@Controller
@RequestMapping("/members")
public class ImageController {





    public final JpaImageRepository jpaImageRepository;
    private final MemberRepository memberRepository;
    JwtUtil jwtUtil = new JwtUtil();

    @Autowired
    public ImageController(JpaImageRepository jpaImageRepository, MemberRepository memberRepository) {
        this.jpaImageRepository = jpaImageRepository;
        this.memberRepository = memberRepository;
    }
//
//    @PostMapping("/newPost")
//    public String imageUpload(
//            @RequestParam(name="imageFile") MultipartFile image,
//            HttpServletRequest request, Model model
//    ) throws IOException {
//
//        String token = CookieUtil.getTokenFromCookie(request);
//        if (token == null) {
//            return "redirect:/";
//        }
//        String username = jwtUtil.validateToken(token);
//        Optional<Member> loginUser = memberRepository.findByName(username);
//
//        Long userId = loginUser.get().getId();
//        // 이미지 파일을 바이트 배열로 변환
//        byte[] imageBytes = image.getBytes();
//
//        // 이미지 파일을 Base64로 인코딩
//        String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
//
//        // Image 객체에 Base64 인코딩된 이미지 저장
//        Image imageBean = new Image();
//        imageBean.setImageFile(imageBase64);
//        imageBean.
//
//        // DB에 저장
//        jpaImageRepository.save(imageBean);
//        model.addAttribute("UploadImage", imageBase64);
//
//        // 프로필 화면으로 이동
//        return "/members/newPost";
//    }

}
