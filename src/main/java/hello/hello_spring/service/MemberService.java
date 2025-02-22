package hello.hello_spring.service;

import hello.hello_spring.controller.MemberForm;
import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.JpaMemberRepository;

import hello.hello_spring.repository.MemberRepository;
import jdk.jfr.Threshold;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//jpa 를 쓰려면 transactional이 있어야 함
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
//    public   Long join(Member member) {
//        // 같은 이름 있는 중복 회원X
//        if (validateDuplicatedMember(member)) {
//            return null;
//        }
//
//        memberRepository.save(member);
//        return member.getId();
//    }

    public boolean validateDuplicatedMember(String name) {
        Optional<Member> optionalMember = memberRepository
                .findByName(name);
        if (optionalMember.isPresent()) {
            return true;
        }
        return false;
//        Optional.ofNullable(memberRepository.findByName(member.getName()))
//                .ifPresent(m ->  {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");
//
//                });

    }

    /**
     * 전체 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(String memberPassword) {
        return memberRepository.findByPassword(memberPassword);
    }

    public boolean deleteMember(MemberForm memberform) {
        Optional<Member> optionalMember = memberRepository.findByName(memberform.getName());

        if (optionalMember.isPresent()) {
            return memberRepository.delete(memberform.getPassword(), memberform.getName()) != 0;
        }
        return false;

    }

    public boolean updatePassword(String name, String password, String newPassword) {

            return memberRepository.changePassword(name, password, newPassword) != 0;

    }

    public boolean updateEmail(String name, String password, String newEmail) {

        if(memberRepository.findByName(name).isPresent()) {
            return memberRepository.changeEmail(name, password, newEmail) != 0;
        }
        else return false;
    }

    public Optional<Member> login(MemberForm memberForm) {
        Optional<Member> optionalMember = memberRepository.findByName(memberForm.getName());

        if (optionalMember.isPresent()) {
            return memberRepository.login(memberForm.getName(), memberForm.getPassword());
        }
        return Optional.empty();
    }
}
