package hello.board.myboard.service;

import hello.board.myboard.common.exception.BadRequestException;
import hello.board.myboard.repository.BoardRepository;
import hello.board.myboard.repository.LikesRepository;
import hello.board.myboard.repository.ReplyRepository;
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
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    @Transactional
    public int likeBoard(Long memberId, Long boardId) {
        // 게시글이 존재하는지 확인
        boardRepository.findById(boardId).orElseThrow(() -> new BadRequestException("게시글이 존재하지 않습니다."));

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
        // 댓글이 존재하는지 확인
        replyRepository.findById(replyId).orElseThrow(() -> new BadRequestException("댓글이 존재하지 않습니다."));

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
