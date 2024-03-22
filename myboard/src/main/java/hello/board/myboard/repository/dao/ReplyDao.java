package hello.board.myboard.repository.dao;


import hello.board.myboard.vo.ReplyVo;
import hello.board.myboard.dto.reply.ReplySearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReplyDao {
    void save(ReplyVo replyVo);
    Optional<ReplyVo> findById(Long id);
    List<ReplyVo> findByBoardId(Long boardId);
    List<ReplyVo> findByMemberId(Long memberId);
    void deleteById(Long id);
    List<ReplyVo> findPageByBoardId(@Param("replySearchDto") ReplySearchDto replySearchDto, @Param("boardId") Long boardId);
    Integer findPageMaxCountByBoardId(Long boardId);
}
