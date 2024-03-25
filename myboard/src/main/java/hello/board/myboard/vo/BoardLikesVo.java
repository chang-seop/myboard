package hello.board.myboard.vo;

import hello.board.myboard.dto.board.BoardDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardLikesVo {
    private Long boardId;
    private Long memberId;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardRegdate;
    private LocalDateTime boardUpdateDate;
    private LocalDateTime boardDeleteDate;
    private Integer likeCount;

    @Builder
    public BoardLikesVo(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContent, LocalDateTime boardRegdate, LocalDateTime boardUpdateDate, LocalDateTime boardDeleteDate, Integer likeCount) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
        this.boardUpdateDate = boardUpdateDate;
        this.boardDeleteDate = boardDeleteDate;
        this.likeCount = likeCount;
    }

    public BoardDto toBoardDto() {
        return BoardDto.builder()
                .boardId(boardId)
                .memberId(memberId)
                .boardWriter(boardWriter)
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .boardRegdate(boardRegdate)
                .boardUpdateDate(boardUpdateDate)
                .boardDeleteDate(boardDeleteDate)
                .boardLikeCount(likeCount)
                .build();
    }
}
