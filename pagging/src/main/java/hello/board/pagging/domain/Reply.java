package hello.board.pagging.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Reply {
    private Long replyId;
    private Long boardId;
    private String writer;
    private String content;
    private LocalDateTime regdate;

    @Builder
    public Reply(Long replyId, Long boardId, String writer, String content, LocalDateTime regdate) {
        this.replyId = replyId;
        this.boardId = boardId;
        this.writer = writer;
        this.content = content;
        this.regdate = regdate;
    }
}
