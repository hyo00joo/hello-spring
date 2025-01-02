package hello.hello_spring.service;

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
    public Long join(Member member) {
        // 같은 이름 있는 중복 회원X
        if (validateDuplicatedMember(member)) {
            return null;
        }

        memberRepository.save(member);
        return member.getId();
    }

    private boolean validateDuplicatedMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findByName(member.getName());
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

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public boolean deleteMember(Member member) {
        Optional<Member> optionalMember = memberRepository.findByName(member.getName());

        if (optionalMember.isPresent()) {
            if(memberRepository.delete(member.getId(), member.getName()) != 0) {
                return true;
            }
        }
        return false;

    }
}
