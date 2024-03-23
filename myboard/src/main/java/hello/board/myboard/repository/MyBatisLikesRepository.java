package hello.board.myboard.repository;

import hello.board.myboard.dto.likes.LikesBoardCountDto;
import hello.board.myboard.repository.dao.LikesDao;
import hello.board.myboard.vo.LikesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisLikesRepository implements LikesRepository {
    private final LikesDao likesDao;
    @Override
    public LikesVo saveBoardLike(LikesVo likesVo) {
        likesDao.saveBoardLike(likesVo);
        return likesVo;
    }

    @Override
    public LikesVo saveReplyLike(LikesVo likesVo) {
        likesDao.saveReplyLike(likesVo);
        return likesVo;
    }

    @Override
    public void deleteByMemberIdAndBoardId(Long memberId, Long boardId) {
        likesDao.deleteByMemberIdAndBoardId(memberId, boardId);
    }

    @Override
    public void deleteByMemberIdAndReplyId(Long memberId, Long replyId) {
        likesDao.deleteByMemberIdAndReplyId(memberId, replyId);
    }

    @Override
    public Optional<LikesVo> findByMemberIdAndBoardId(Long memberId, Long boardId) {
        return likesDao.findByMemberIdAndBoardId(memberId, boardId);
    }

    @Override
    public Optional<LikesVo> findByMemberIdAndReplyId(Long memberId, Long replyId) {
        return likesDao.findByMemberIdAndReplyId(memberId, replyId);
    }

    @Override
    public Integer findCountByBoardId(Long boardId) {
        return likesDao.findCountByBoardId(boardId);
    }

    @Override
    public Integer findCountByReplyId(Long replyId) {
        return likesDao.findCountByReplyId(replyId);
    }

    @Override
    public List<LikesBoardCountDto> findBoardLikeByBoardIdList(List<Long> boardIdList) {
        return likesDao.findBoardLikeByBoardIdList(boardIdList);
    }
}
