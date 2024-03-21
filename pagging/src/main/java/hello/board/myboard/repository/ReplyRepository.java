package hello.board.myboard.repository;

import hello.board.myboard.domain.Reply;
import hello.board.myboard.model.reply.ReplySearchDto;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    Reply save(Reply reply);
    Optional<Reply> findById(Long id);
    List<Reply> findByBoardId(Long boardId);
    List<Reply> findByMemberId(Long memberId);
    void deleteById(Long id);
    List<Reply> findPageByBoardId(ReplySearchDto replySearchDto, Long boardId);
    Integer findPageMaxCountByBoardId(Long boardId);
}
