package hello.board.myboard.repository.mybatis;

import hello.board.myboard.domain.Board;
import hello.board.myboard.domain.BoardFile;
import hello.board.myboard.model.board.BoardSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardMapper {
    void save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    List<Board> findAll(BoardSearchDto search);
    Integer getPageMaxCount(BoardSearchDto search);
    Optional<BoardFile> findBoardFileById(Long id);
    void deleteSetupByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
    void updateByBoard(Board board);
    List<Board> findDeleteSetup();
    void remove(Long boardId);
    List<Board> findDeleteSetupByMemberId(Long memberId);
    int updateRecoverByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}
