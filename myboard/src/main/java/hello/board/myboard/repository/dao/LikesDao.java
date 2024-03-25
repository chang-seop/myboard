package hello.board.myboard.repository.dao;

import hello.board.myboard.dto.likes.LikesBoardCountDto;
import hello.board.myboard.dto.likes.LikesReplyCountDto;
import hello.board.myboard.vo.LikesVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LikesDao {
    void saveBoardLike(LikesVo likesVo);
    void saveReplyLike(LikesVo likesVo);
    void deleteByMemberIdAndBoardId(@Param("memberId") Long memberId,
                                    @Param("boardId") Long boardId);
    void deleteByMemberIdAndReplyId(@Param("memberId") Long memberId,
                                    @Param("replyId") Long replyId);
    Optional<LikesVo> findByMemberIdAndBoardId(@Param("memberId") Long memberId,
                                               @Param("boardId") Long boardId);
    Optional<LikesVo> findByMemberIdAndReplyId(@Param("memberId") Long memberId,
                                               @Param("replyId") Long replyId);
    Integer findCountByBoardId(Long boardId);
    Integer findCountByReplyId(Long replyId);
    List<LikesReplyCountDto> findReplyLikesCountByReplyIdListAndMemberId(@Param("replyIdList") List<Long> replyIdList,
                                                                         @Param("memberId") Long memberId);
}
