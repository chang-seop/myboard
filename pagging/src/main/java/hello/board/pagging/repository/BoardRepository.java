package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.model.board.SearchDto;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Board save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    /**
     * @param startPosition 조회 시작 위치 (0부터 시작)
     * @param maxResult 조회할 데이터 수
     * @return List
     */
    List<Board> findAll(SearchDto params);
    Integer getPageMaxCount();
    Optional<BoardFile> findByIdWithFile(Long id);
}
