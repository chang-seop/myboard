package hello.board.myboard.model.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ReplyDto {
    private Long replyId;
    private Long boardId;
    private Long memberId;
    private String replyWriter;
    private String replyContent;
    private LocalDateTime replyRegdate;

    @Builder
    public ReplyDto(Long replyId, Long boardId, Long memberId, String replyWriter, String replyContent, LocalDateTime replyRegdate) {
        this.replyId = replyId;
        this.boardId = boardId;
        this.memberId = memberId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
    }
}
