package hello.board.pagging.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardDto {
    private Long boardId;
    private Long memberId;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardRegdate;
    @Builder
    public BoardDto(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContent, LocalDateTime boardRegdate) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
    }
}
