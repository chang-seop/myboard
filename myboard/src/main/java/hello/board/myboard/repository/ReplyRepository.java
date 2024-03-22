package hello.board.myboard.repository;

import hello.board.myboard.vo.ReplyVo;
import hello.board.myboard.dto.reply.ReplySearchDto;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    ReplyVo save(ReplyVo replyVo);
    Optional<ReplyVo> findById(Long id);
    List<ReplyVo> findByBoardId(Long boardId);
    List<ReplyVo> findByMemberId(Long memberId);
    void deleteById(Long id);
    List<ReplyVo> findPageByBoardId(ReplySearchDto replySearchDto, Long boardId);
    Integer findPageMaxCountByBoardId(Long boardId);
}
