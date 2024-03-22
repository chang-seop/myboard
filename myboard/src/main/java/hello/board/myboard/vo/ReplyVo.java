package hello.board.myboard.vo;

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
}
