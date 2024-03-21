package hello.board.myboard.repository.mybatis;

import hello.board.myboard.domain.Authority;
import hello.board.myboard.domain.Member;
import hello.board.myboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisMemberRepository implements MemberRepository {
    private final MemberMapper memberMapper;
    @Override
    public Member save(Member user) {
        memberMapper.save(user);
        return user;
    }

    @Override
    public void insertAuthority(List<Authority> authorities) {
        memberMapper.insertAuthority(authorities);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    @Override
    public Optional<Member> findByName(String name) {
        return memberMapper.findByName(name);
    }
}
