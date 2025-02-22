//package hello.hello_spring.repository;
//
//import hello.hello_spring.domain.Member;
//
//import java.util.*;
//
//
//
//public class MemoryMemberRepository implements MemberRepository {
//
//
//    public static Map<Long, Member> store = new HashMap<>();
//    private static long sequence = 0L;
//    @Override
//    public Member save(Member member) {
////        member.setId(++sequence);
////        store.put(member.getId(), member);
//        return member;
//    }
//
//    @Override
//    public Optional<Member> findByPassword(String password) {
//        return Optional.ofNullable(store.get(password));
//    }
//
//    @Override
//    public Optional<Member> findByName(String name) {
//        return store.values().stream()
//                .filter(member -> member.getName().equals(name))
//                .findAny();
//    }
//
//    @Override
//    public List<Member> findAll() {
//        return new ArrayList<>(store.values());
//
//    }
//
//    @Override
//    public int delete(String password, String name) {
//        store.entrySet().removeIf(entry ->
//                false);
//        return store.size();
//    }
//    public static void clearstore() {
//        store.clear();
//    }
//
//    @Override
//    public Long countMember() {
//        return null;
//    }
//
//    @Override
//    public int changePassword(String password, String name) {
//        return 1;
//    }
//}
