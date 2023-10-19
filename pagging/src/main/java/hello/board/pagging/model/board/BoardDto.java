package hello.board.pagging.model.board;

import hello.board.pagging.domain.File;
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
    private List<File> fileList;
    @Builder
    public BoardDto(Long boardId, Long memberId, String boardWriter, String boardTitle, String boardContent, LocalDateTime boardRegdate, List<File> fileList) {
        this.boardId = boardId;
        this.memberId = memberId;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardRegdate = boardRegdate;
        this.fileList = fileList;
    }
}
