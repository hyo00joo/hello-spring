package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.security.CookieUtil;
import hello.hello_spring.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


//static 보다 우선순위
@Controller
public class HomeController {
   JwtUtil jwtUtil = new JwtUtil();
    @GetMapping("/") //dome의 첫번째 (localhost:8080)
    public String home(HttpServletRequest request) {
//        String token = CookieUtil.getTokenFromCookie(request);
//        String username = jwtUtil.validateToken(token);
//
//        if (username == null) {
//            return "redirect:/"; // 세션이 없으면 로그인 페이지로 리다이렉트
//        }
        return "home";
    }
}
