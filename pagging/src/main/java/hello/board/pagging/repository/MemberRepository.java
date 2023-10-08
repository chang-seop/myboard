package hello.board.pagging.repository;

import hello.board.pagging.domain.Authority;
import hello.board.pagging.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member user);
    void insertAuthority(List<Authority> authorities);
    Optional<Member> findByEmail(String email);
}
