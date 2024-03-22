package hello.board.myboard.repository;

import hello.board.myboard.vo.AuthorityVo;
import hello.board.myboard.vo.MemberVo;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    MemberVo save(MemberVo user);
    void insertAuthority(List<AuthorityVo> authorities);
    Optional<MemberVo> findByEmail(String email);
    Optional<MemberVo> findByName(String name);
}
