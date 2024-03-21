package hello.board.myboard.model.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ReplySaveDto {
    private String replyContent;
    @Builder
    public ReplySaveDto(String replyContent) {
        this.replyContent = replyContent;
    }
}
