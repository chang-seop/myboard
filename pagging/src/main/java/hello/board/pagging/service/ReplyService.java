package hello.board.pagging.service;

import hello.board.pagging.common.exception.BadRequestException;
import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.Member;
import hello.board.pagging.domain.Reply;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.PagingResponseDto;
import hello.board.pagging.model.reply.ReplyDto;
import hello.board.pagging.model.reply.ReplySaveDto;
import hello.board.pagging.model.reply.ReplySearchDto;
import hello.board.pagging.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
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
     * 댓글 조회 (페이징 처리)
     */
    @Transactional(readOnly = true)
    public PagingResponseDto<ReplyDto> findPageReply(ReplySearchDto replySearchDto, Long boardId) {
        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null 을 담아 반환
        Integer count = replyRepository.findPageMaxCountByBoardId(boardId);

        if (count < 1) {
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 replySearchDto 에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, replySearchDto);
        replySearchDto.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
        List<Reply> findReplyList = replyRepository.findPageByBoardId(replySearchDto, boardId);

        List<ReplyDto> replyDtoList = new ArrayList<>();
        findReplyList.forEach(reply -> replyDtoList.add(ReplyDto.builder()
                        .replyId(reply.getReplyId())
                        .boardId(reply.getBoardId())
                        .memberId(reply.getMemberId())
                        .replyWriter(reply.getReplyWriter())
                        .replyContent(reply.getReplyContent())
                        .replyRegdate(reply.getReplyRegdate())
                        .build()));

        return new PagingResponseDto<>(replyDtoList, pagination);
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
