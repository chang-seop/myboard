package hello.board.myboard.vo;

import hello.board.myboard.dto.reply.ReplyDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyVo {
    private Long replyId;
    private Long boardId;
    private Long memberId;
    private String replyWriter;
    private String replyContent;
    private LocalDateTime replyRegdate;

    @Builder
    public ReplyVo(Long replyId, Long boardId, Long memberId, String replyWriter, String replyContent, LocalDateTime replyRegdate) {
        this.replyId = replyId;
        this.boardId = boardId;
        this.memberId = memberId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
    }

    public ReplyDto toDto() {
        return ReplyDto.builder()
                .replyId(replyId)
                .boardId(boardId)
                .memberId(memberId)
                .replyWriter(replyWriter)
                .replyContent(replyContent)
                .replyRegdate(replyRegdate)
                .build();
    }
}
