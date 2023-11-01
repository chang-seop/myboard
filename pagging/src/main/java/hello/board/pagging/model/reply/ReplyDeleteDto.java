package hello.board.pagging.model.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReplyDeleteDto {
    private Long replyId;
    private Long boardId;

    @Builder
    public ReplyDeleteDto(Long replyId, Long boardId) {
        this.replyId = replyId;
        this.boardId = boardId;
    }
}
