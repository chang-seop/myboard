package hello.board.pagging.model.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
@NoArgsConstructor
public class ReplySaveDto {
    private String replyContent;
    @Builder
    public ReplySaveDto(String replyContent) {
        this.replyContent = replyContent;
    }
}
