package hello.board.myboard.repository;

import hello.board.myboard.dto.likes.LikesBoardCountDto;
import hello.board.myboard.vo.LikesVo;

import java.util.List;
import java.util.Optional;

public interface LikesRepository {
    LikesVo saveBoardLike(LikesVo likesVo);
    LikesVo saveReplyLike(LikesVo likesVo);
    void deleteByMemberIdAndBoardId(Long memberId, Long boardId);
    void deleteByMemberIdAndReplyId(Long memberId, Long replyId);
    Optional<LikesVo> findByMemberIdAndBoardId(Long memberId, Long boardId);
    Optional<LikesVo> findByMemberIdAndReplyId(Long memberId, Long replyId);
    Integer findCountByBoardId(Long boardId);
    Integer findCountByReplyId(Long replyId);
    List<LikesBoardCountDto> findBoardLikeByBoardIdList(List<Long> boardIdList);
}
