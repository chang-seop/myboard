package hello.board.myboard.dto.board;

import hello.board.myboard.vo.FileVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class BoardDto {
    private Long boardId;
    private Long memberId;
    private String boardWriter;
    private String boardTitle;
    private String boardContent;
    private LocalDateTime boardRegdate;
    private LocalDateTime boardUpdateDate;
    private LocalDateTime boardDeleteDate;
    private Integer boardLikeCount;
    private int myBoardLikeCount;
    private List<FileVo> fileList;
    @Builder
    public BoardDto(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContent, LocalDateTime boardRegdate, LocalDateTime boardUpdateDate, LocalDateTime boardDeleteDate, Integer boardLikeCount, int myBoardLikeCount, List<FileVo> fileList) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
        this.boardUpdateDate = boardUpdateDate;
        this.boardDeleteDate = boardDeleteDate;
        this.boardLikeCount = boardLikeCount;
        this.myBoardLikeCount = myBoardLikeCount;
        this.fileList = fileList;
    }
}
