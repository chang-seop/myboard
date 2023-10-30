package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.model.Search;
import hello.board.pagging.model.board.BoardSearchDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    List<Board> findAll(BoardSearchDto params);
    Integer getPageMaxCount();
    Optional<BoardFile> findBoardFileById(Long id);
    void deleteSetupByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
    void updateByBoard(Board board);
}
