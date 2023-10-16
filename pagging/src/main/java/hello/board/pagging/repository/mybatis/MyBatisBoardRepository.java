package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.Board;
import hello.board.pagging.model.board.SearchDto;
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
    public List<Board> findAll(SearchDto searchDto) {
        return boardMapper.findAll(searchDto);
    }

    @Override
    public Integer getPageMaxCount() {
        return boardMapper.getPageMaxCount();
    }
}
