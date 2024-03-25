package hello.board.myboard.repository;

import hello.board.myboard.repository.dao.BoardDao;
import hello.board.myboard.vo.BoardFileVo;
import hello.board.myboard.vo.BoardLikesVo;
import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.dto.board.BoardSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisBoardRepository implements BoardRepository {
    private final BoardDao boardDao;

    @Override
    public BoardVo save(BoardVo boardVo) {
        boardDao.save(boardVo);
        return boardVo;
    }

    @Override
    public Optional<BoardVo> findById(Long id) {
        return boardDao.findById(id);
    }

    @Override
    public List<BoardVo> findByMemberId(Long memberId) {
        return boardDao.findByMemberId(memberId);
    }

    @Override
    public List<BoardLikesVo> findAll(BoardSearchDto search) {
        return boardDao.findAll(search);
    }

    @Override
    public Integer getPageMaxCount(BoardSearchDto search) {
        return boardDao.getPageMaxCount(search);
    }

    @Override
    public Optional<BoardFileVo> findBoardFileById(Long id) {
        return boardDao.findBoardFileById(id);
    }

    @Override
    public void deleteSetupByBoardIdAndMemberId(Long boardId, Long memberId) {
        boardDao.deleteSetupByBoardIdAndMemberId(boardId, memberId);
    }

    @Override
    public void updateByBoard(BoardVo boardVo) {
        boardDao.updateByBoard(boardVo);
    }

    @Override
    public List<BoardVo> findDeleteSetup() {
        return boardDao.findDeleteSetup();
    }

    @Override
    public void remove(Long boardId) {
        boardDao.remove(boardId);
    }

    @Override
    public List<BoardVo> findDeleteSetupByMemberId(Long memberId) {
        return boardDao.findDeleteSetupByMemberId(memberId);
    }

    @Override
    public int updateRecoverByBoardIdAndMemberId(Long boardId, Long memberId) {
        return boardDao.updateRecoverByBoardIdAndMemberId(boardId, memberId);
    }
}
