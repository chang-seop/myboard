package hello.board.myboard.repository.mybatis;

import hello.board.myboard.domain.Authority;
import hello.board.myboard.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    void insertAuthority(List<Authority> authorities);
    Optional<Member> findByEmail(@Param("email") String email);
    Optional<Member> findByName(String name);
}
