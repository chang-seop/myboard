package hello.board.myboard.repository;

import hello.board.myboard.domain.Board;
import hello.board.myboard.domain.BoardFile;
import hello.board.myboard.model.board.BoardSearchDto;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    List<Board> findAll(BoardSearchDto params);
    Integer getPageMaxCount(BoardSearchDto search);
    Optional<BoardFile> findBoardFileById(Long id);
    void deleteSetupByBoardIdAndMemberId(Long boardId, Long memberId);
    void updateByBoard(Board board);
    List<Board> findDeleteSetup();
    void remove(Long boardId);
    List<Board> findDeleteSetupByMemberId(Long memberId);
    int updateRecoverByBoardIdAndMemberId(Long boardId, Long memberId);
}
