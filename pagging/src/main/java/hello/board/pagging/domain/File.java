package hello.board.pagging.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {
    private Long fileId;
    private String fileName;
    private LocalDateTime fileRegdate;

    @Builder
    public File(Long fileId, String fileName, LocalDateTime fileRegdate) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileRegdate = fileRegdate;
    }
}
