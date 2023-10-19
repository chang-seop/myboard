package hello.board.pagging.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 *  게시판 저장용 폼
 */
@Getter @Setter
public class BoardSaveDto {
    @NotBlank(message = "제목을 입력해주시기 바랍니다.")
    private String boardTitle;
    @NotBlank(message = "내용을 입력해주시길 바랍니다.")
    private String boardContent;
    private List<MultipartFile> imageFiles; // 이미지를 다중 업로드 하기 위해 MultipartFile 사용
    private MultipartFile attachFile; // 첨부 파일

    @Builder
    public BoardSaveDto(String boardTitle, String boardContent) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
    }
}
