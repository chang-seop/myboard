package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.model.Search;
import hello.board.pagging.model.board.BoardSearchDto;
import hello.board.pagging.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisBoardRepository implements BoardRepository {
    private final BoardMapper boardMapper;

    @Override
    public Board save(Board board) {
        boardMapper.save(board);
        return board;
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardMapper.findById(id);
    }

    @Override
    public List<Board> findByMemberId(Long memberId) {
        return boardMapper.findByMemberId(memberId);
    }

    @Override
    public List<Board> findAll(BoardSearchDto search) {
        return boardMapper.findAll(search);
    }

    @Override
    public Integer getPageMaxCount(BoardSearchDto search) {
        return boardMapper.getPageMaxCount(search);
    }

    @Override
    public Optional<BoardFile> findBoardFileById(Long id) {
        return boardMapper.findBoardFileById(id);
    }

    @Override
    public void deleteSetupByBoardIdAndMemberId(Long boardId, Long memberId) {
        boardMapper.deleteSetupByBoardIdAndMemberId(boardId, memberId);
    }

    @Override
    public void updateByBoard(Board board) {
        boardMapper.updateByBoard(board);
    }

    @Override
    public List<Board> findDeleteSetup() {
        return boardMapper.findDeleteSetup();
    }

    @Override
    public void remove(Long boardId) {
        boardMapper.remove(boardId);
    }
}
