package hello.board.myboard.model.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 게시판 삭제용 폼
 */
@Getter @Setter
public class BoardDeleteDto {
    private Long boardId;

    @Builder
    public BoardDeleteDto(Long boardId) {
        this.boardId = boardId;
    }
}
