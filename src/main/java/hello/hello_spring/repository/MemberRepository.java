package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import hello.hello_spring.domain.Post;
import hello.hello_spring.domain.ProfileImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;




public interface MemberRepository
{
    Member save(Member member);
    Optional<Member> findByPassword(String password);
    Optional<Member> findByName(String name);
    Optional<Member> findById(Long id);
    List<Member> findAll();
    int delete(String password, String name);
    Long countMember();
    int changePassword(String name, String Password,String newPassword);
    int changeEmail(String name, String password, String newEmail);
    Optional<Member> login(String name, String password);
//    void saveProfileImage(ProfileImage profileImage);


}

