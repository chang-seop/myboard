package hello.board.myboard.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {
    private Long fileId;
    private Long boardId;
    private String uploadFileName;
    private String storeFileName;
    private Boolean fileImageYn;
    private LocalDateTime fileRegdate;

    @Builder
    public File(Long fileId, Long boardId, String uploadFileName, String storeFileName, Boolean fileImageYn, LocalDateTime fileRegdate) {
        this.fileId = fileId;
        this.boardId = boardId;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileImageYn = fileImageYn;
        this.fileRegdate = fileRegdate;
    }
}
