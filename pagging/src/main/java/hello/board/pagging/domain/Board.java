package hello.board.pagging.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Board {
    private Long boardId;
    private Long memberId;
    private String writer;
    private String title;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime updatedate;
    private LocalDateTime deletedate;

    @Builder
    public Board(Long boardId, Long memberId, String writer, String title, String content, LocalDateTime regdate, LocalDateTime updatedate, LocalDateTime deletedate) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.regdate = regdate;
        this.updatedate = updatedate;
        this.deletedate = deletedate;
    }
}
