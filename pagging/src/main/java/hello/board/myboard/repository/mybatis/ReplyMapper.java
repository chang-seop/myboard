package hello.board.myboard.repository.mybatis;


import hello.board.myboard.domain.Reply;
import hello.board.myboard.model.reply.ReplySearchDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReplyMapper {
    void save(Reply reply);
    Optional<Reply> findById(Long id);
    List<Reply> findByBoardId(Long boardId);
    List<Reply> findByMemberId(Long memberId);
    void deleteById(Long id);
    List<Reply> findPageByBoardId(@Param("replySearchDto") ReplySearchDto replySearchDto, @Param("boardId") Long boardId);
    Integer findPageMaxCountByBoardId(Long boardId);
}
