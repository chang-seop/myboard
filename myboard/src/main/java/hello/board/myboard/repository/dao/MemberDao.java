package hello.board.myboard.repository.dao;

import hello.board.myboard.vo.AuthorityVo;
import hello.board.myboard.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberDao {
    void save(MemberVo memberVo);
    void insertAuthority(List<AuthorityVo> authorities);
    Optional<MemberVo> findByEmail(@Param("email") String email);
    Optional<MemberVo> findByName(String name);
}
