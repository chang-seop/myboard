package hello.board.myboard.repository;

import hello.board.myboard.dto.board.BoardDto;
import hello.board.myboard.vo.BoardFileVo;
import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.dto.board.BoardSearchDto;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    BoardVo save(BoardVo boardVo);
    Optional<BoardVo> findById(Long id);
    List<BoardVo> findByMemberId(Long memberId);
    List<BoardVo> findAll(BoardSearchDto params);
    Integer getPageMaxCount(BoardSearchDto search);
    Optional<BoardFileVo> findBoardFileById(Long id);
    void deleteSetupByBoardIdAndMemberId(Long boardId, Long memberId);
    void updateByBoard(BoardVo boardVo);
    List<BoardVo> findDeleteSetup();
    void remove(Long boardId);
    List<BoardVo> findDeleteSetupByMemberId(Long memberId);
    int updateRecoverByBoardIdAndMemberId(Long boardId, Long memberId);
}
