package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.ProfileImage;
import hello.hello_spring.repository.JpaImageRepository;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.security.CookieUtil;
import hello.hello_spring.security.JwtUtil;
import hello.hello_spring.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
public class S3FileController {
    private final S3Service s3Service;

    public S3FileController(S3Service s3Service, MemberRepository memberRepository, JpaImageRepository jpaImageRepository) {
        this.s3Service = s3Service;
        this.memberRepository = memberRepository;
        this.jpaImageRepository = jpaImageRepository;
    }

    private final JwtUtil jwtUtil = new JwtUtil();
    private final MemberRepository memberRepository;
    private final JpaImageRepository jpaImageRepository;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            String username = jwtUtil.validateToken(token);
            Member member = memberRepository.findByName(username).get();
            String profileImageFile = member.getProfileImage().getImageFile();
            System.out.println(profileImageFile);
            if (profileImageFile != null) {
                s3Service.deleteImage(profileImageFile); // S3에서 삭제
            }

            String newFileUrl = s3Service.uploadImage(file);
            member.getProfileImage().setImageFile(newFileUrl);
            jpaImageRepository.saveProfileImage(member.getProfileImage());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body("파일 업로드 실패: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteFile(HttpServletRequest request) {
        String token = CookieUtil.getTokenFromCookie(request);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = jwtUtil.validateToken(token);
        Member member = memberRepository.findByName(username).get();
        jpaImageRepository.deleteProfileImageById(member.getId());
        s3Service.deleteImage(member.getProfileImage().getImageFile());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
