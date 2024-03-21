package hello.board.myboard.repository;

import hello.board.myboard.domain.Authority;
import hello.board.myboard.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member user);
    void insertAuthority(List<Authority> authorities);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByName(String name);
}
