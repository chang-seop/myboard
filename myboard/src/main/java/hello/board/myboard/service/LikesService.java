package hello.board.myboard.service;

import hello.board.myboard.repository.LikesRepository;
import hello.board.myboard.vo.LikesVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    @Transactional
    public int likeBoard(Long memberId, Long boardId) {
        Optional<LikesVo> optionalLikes = likesRepository.findByMemberIdAndBoardId(memberId, boardId);

        // 이미 좋아요를 누른 상태일 경우
        if(optionalLikes.isPresent()) {
            likesRepository.deleteByMemberIdAndBoardId(memberId, boardId);
            return -1;
        }

        // 아닐경우
        LikesVo likesVo = LikesVo.builder()
                .memberId(memberId)
                .boardId(boardId)
                .build();

        likesRepository.saveBoardLike(likesVo);
        return 1;
    }
    @Transactional
    public int likeReply(Long memberId, Long replyId) {
        Optional<LikesVo> optionalLikes = likesRepository.findByMemberIdAndReplyId(memberId, replyId);

        // 이미 좋아요를 누른 상태일 경우
        if(optionalLikes.isPresent()) {
            likesRepository.deleteByMemberIdAndReplyId(memberId, replyId);
            return -1;
        }

        // 아닐경우
        LikesVo likesVo = LikesVo.builder()
                .memberId(memberId)
                .replyId(replyId)
                .build();

        likesRepository.saveReplyLike(likesVo);
        return 1;
    }

    public int getLikeBoard(Long memberId, Long boardId) {
        Optional<LikesVo> optionalLikes = likesRepository.findByMemberIdAndBoardId(memberId, boardId);
        if(optionalLikes.isPresent()) {
            return 1;
        }
        return -1;
    }
}
