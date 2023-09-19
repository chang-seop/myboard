package hello.board.pagging.repository;

import hello.board.pagging.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member user);
    Optional<Member> findByEmail(String email);
}
