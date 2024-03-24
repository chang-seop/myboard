package hello.board.myboard.dto.reply;

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
    private Integer replyLikeCount;
    private Integer myReplyLikeCount;

    @Builder
    public ReplyDto(Long replyId, Long boardId, Long memberId, String replyWriter, String replyContent, LocalDateTime replyRegdate, Integer replyLikeCount, Integer myReplyLikeCount) {
        this.replyId = replyId;
        this.boardId = boardId;
        this.memberId = memberId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
        this.replyLikeCount = replyLikeCount;
        this.myReplyLikeCount = myReplyLikeCount;
    }
}
