package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Board create(Board board);
    Optional<Board> findById(Long id);
    List<Board> findByMemberId(Long memberId);
    /**
     * @param startPosition 조회 시작 위치 (0부터 시작)
     * @param maxResult 조회할 데이터 수
     * @return List
     */
    List<Board> findAll(int startPosition, int maxResult);
    Integer getMaxCount();
}
