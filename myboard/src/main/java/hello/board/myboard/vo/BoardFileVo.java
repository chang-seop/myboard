package hello.board.myboard.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardFileVo {
    private Long boardId;
    private Long memberId;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardRegdate;
    private LocalDateTime boardUpdateDate;
    private LocalDateTime boardDeleteDate;
    private Integer likeCount;
    private List<FileVo> fileVoList;
    @Builder
    public BoardFileVo(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContent, LocalDateTime boardRegdate, LocalDateTime boardUpdateDate, LocalDateTime boardDeleteDate, Integer likeCount, List<FileVo> fileVoList) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
        this.boardUpdateDate = boardUpdateDate;
        this.boardDeleteDate = boardDeleteDate;
        this.likeCount = likeCount;
        this.fileVoList = fileVoList;
    }
}
