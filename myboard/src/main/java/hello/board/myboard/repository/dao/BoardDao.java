package hello.board.myboard.repository.dao;

import hello.board.myboard.vo.BoardFileVo;
import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.dto.board.BoardSearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BoardDao {
    void save(BoardVo boardVo);
    Optional<BoardVo> findById(Long id);
    List<BoardVo> findByMemberId(Long memberId);
    List<BoardVo> findAll(BoardSearchDto search);
    Integer getPageMaxCount(BoardSearchDto search);
    Optional<BoardFileVo> findBoardFileById(Long id);
    void deleteSetupByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
    void updateByBoard(BoardVo boardVo);
    List<BoardVo> findDeleteSetup();
    void remove(Long boardId);
    List<BoardVo> findDeleteSetupByMemberId(Long memberId);
    int updateRecoverByBoardIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}
