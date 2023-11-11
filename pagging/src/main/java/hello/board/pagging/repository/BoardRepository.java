package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.model.board.BoardSearchDto;

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
