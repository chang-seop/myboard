package hello.board.myboard.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileVo {
    private Long fileId;
    private Long boardId;
    private String uploadFileName;
    private String storeFileName;
    private Boolean fileImageYn;
    private LocalDateTime fileRegdate;

    @Builder
    public FileVo(Long fileId, Long boardId, String uploadFileName, String storeFileName, Boolean fileImageYn, LocalDateTime fileRegdate) {
        this.fileId = fileId;
        this.boardId = boardId;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.fileImageYn = fileImageYn;
        this.fileRegdate = fileRegdate;
    }
}
