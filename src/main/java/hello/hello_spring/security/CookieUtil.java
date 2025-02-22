package hello.hello_spring.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void addTokenToCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // HttpOnly 설정
        cookie.setSecure(true);  // HTTPS 환경에서만 전송
        cookie.setPath("/");     // 전체 경로에 적용
        cookie.setMaxAge(60 * 60); // 쿠키 유효 시간 (초 단위)

        response.addCookie(cookie);
    }

    public static String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
