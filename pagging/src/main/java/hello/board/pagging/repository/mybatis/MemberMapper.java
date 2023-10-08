package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.Authority;
import hello.board.pagging.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    void save(Member member);
    void insertAuthority(List<Authority> authorities);
    Optional<Member> findByEmail(@Param("email") String email);
}
