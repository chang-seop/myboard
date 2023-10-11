package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    List<Board> findAll(@Param("startPosition") int startPosition, @Param("maxResult") int maxResult, @Param("title") boolean title);
    Integer getPageMaxCount();
}
