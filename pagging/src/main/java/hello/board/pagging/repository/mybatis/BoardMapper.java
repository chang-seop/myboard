package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.model.board.SearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    List<Board> findAll(SearchDto searchDto);
    Integer getPageMaxCount();
    Optional<BoardFile> findByIdWithFile(Long id);
    void deleteSetupByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
    void updateByBoard(Board board);
}
