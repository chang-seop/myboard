package hello.board.myboard.domain;

import hello.board.myboard.model.board.BoardDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {
    private Long boardId;
    private Long memberId;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardRegdate;
    private LocalDateTime boardUpdateDate;
    private LocalDateTime boardDeleteDate;

    @Builder
    public Board(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContent, LocalDateTime boardRegdate, LocalDateTime boardUpdateDate, LocalDateTime boardDeleteDate) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
        this.boardUpdateDate = boardUpdateDate;
        this.boardDeleteDate = boardDeleteDate;
    }

    public BoardDto toDto() {
        return BoardDto.builder()
                .boardId(boardId)
                .memberId(memberId)
                .boardWriter(boardWriter)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardRegdate(boardRegdate)
                .boardUpdateDate(boardUpdateDate)
                .boardDeleteDate(boardDeleteDate)
                .build();
    }
}
