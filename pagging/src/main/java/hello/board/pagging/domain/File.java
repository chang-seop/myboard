package hello.board.pagging.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class File {
    private Long fileId;
    private String filename;
    private LocalDateTime regdate;

    @Builder
    public File(Long fileId, String filename, LocalDateTime regdate) {
        this.fileId = fileId;
        this.filename = filename;
        this.regdate = regdate;
    }
}
