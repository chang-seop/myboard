package hello.board.myboard.repository.mybatis;

import hello.board.myboard.domain.Reply;
import hello.board.myboard.model.reply.ReplySearchDto;
import hello.board.myboard.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisReplyRepository implements ReplyRepository {
    private final ReplyMapper replyMapper;
    @Override
    public Reply save(Reply reply) {
        replyMapper.save(reply);
        return reply;
    }

    @Override
    public Optional<Reply> findById(Long id) {
        return replyMapper.findById(id);
    }

    @Override
    public List<Reply> findByBoardId(Long boardId) {
        return replyMapper.findByBoardId(boardId);
    }

    @Override
    public List<Reply> findByMemberId(Long memberId) {
        return replyMapper.findByMemberId(memberId);
    }

    @Override
    public void deleteById(Long id) {
        replyMapper.deleteById(id);
    }

    @Override
    public List<Reply> findPageByBoardId(ReplySearchDto replySearchDto, Long boardId) {
        return replyMapper.findPageByBoardId(replySearchDto, boardId);
    }

    @Override
    public Integer findPageMaxCountByBoardId(Long boardId) {
        return replyMapper.findPageMaxCountByBoardId(boardId);
    }
}
