package hello.board.myboard.repository;

import hello.board.myboard.repository.dao.ReplyDao;
import hello.board.myboard.vo.ReplyVo;
import hello.board.myboard.dto.reply.ReplySearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisReplyRepository implements ReplyRepository {
    private final ReplyDao replyDao;
    @Override
    public ReplyVo save(ReplyVo replyVo) {
        replyDao.save(replyVo);
        return replyVo;
    }

    @Override
    public Optional<ReplyVo> findById(Long id) {
        return replyDao.findById(id);
    }

    @Override
    public List<ReplyVo> findByBoardId(Long boardId) {
        return replyDao.findByBoardId(boardId);
    }

    @Override
    public List<ReplyVo> findByMemberId(Long memberId) {
        return replyDao.findByMemberId(memberId);
    }

    @Override
    public void deleteById(Long id) {
        replyDao.deleteById(id);
    }

    @Override
    public List<ReplyVo> findPageByBoardId(ReplySearchDto replySearchDto, Long boardId) {
        return replyDao.findPageByBoardId(replySearchDto, boardId);
    }

    @Override
    public Integer findPageMaxCountByBoardId(Long boardId) {
        return replyDao.findPageMaxCountByBoardId(boardId);
    }
}
