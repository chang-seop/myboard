package hello.board.pagging.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {
    private Long replyId;
    private Long boardId;
    private Long memberId;
    private String replyWriter;
    private String replyContent;
    private LocalDateTime replyRegdate;

    @Builder
    public Reply(Long replyId, Long boardId, Long memberId, String replyWriter, String replyContent, LocalDateTime replyRegdate) {
        this.replyId = replyId;
        this.boardId = boardId;
        this.memberId = memberId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.replyRegdate = replyRegdate;
    }
}
