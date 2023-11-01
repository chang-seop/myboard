package hello.board.pagging.service;

import hello.board.pagging.common.exception.BadRequestException;
import hello.board.pagging.domain.Member;
import hello.board.pagging.domain.Reply;
import hello.board.pagging.model.reply.ReplyDto;
import hello.board.pagging.model.reply.ReplySaveDto;
import hello.board.pagging.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;

    /**
     * 댓글 저장
     */
    @Transactional
    public void saveReply(ReplySaveDto replySaveDto, Long boardId, Member member) {
        Reply reply = Reply.builder()
                .boardId(boardId)
                .memberId(member.getMemberId())
                .replyWriter(member.getMemberNm())
                .replyContent(replySaveDto.getReplyContent())
                .build();

        replyRepository.save(reply);
    }

    /**
     * 댓글 조회
     */
    @Transactional(readOnly = true)
    public List<ReplyDto> findReply(Long boardId) {
        List<Reply> findReply = replyRepository.findByBoardId(boardId);

        return findReply.stream()
                .map((reply) -> ReplyDto.builder()
                        .replyId(reply.getReplyId())
                        .boardId(reply.getBoardId())
                        .memberId(reply.getMemberId())
                        .replyWriter(reply.getReplyWriter())
                        .replyContent(reply.getReplyContent())
                        .replyRegdate(reply.getReplyRegdate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 댓글 삭제
     * 댓글이 존재하지 않음, 사용자의 댓글이 아님 언체크 예외 throw
     */
    @Transactional
    public void deleteReply(Long replyId, Long boardId, Member member) {
        Optional<Reply> findReply = replyRepository.findById(replyId);

        // 댓글이 존재하는지 확인
        Reply reply = findReply.orElseThrow(() -> new BadRequestException("댓글이 존재하지 않습니다."));

        // 사용자의 댓글인지 확인 db 에 조회한 reply.getMemberId 가 세션으로 얻은 memberId 값이 같은지 확인
        if(!reply.getMemberId().equals(member.getMemberId())) {
            throw new BadRequestException("사용자의 댓글이 아닙니다.");
        }

        replyRepository.deleteById(replyId);
    }
}
