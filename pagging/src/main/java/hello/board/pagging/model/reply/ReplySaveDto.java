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
    @NotBlank(message = "댓글을 입력해주시길 바랍니다.")
    @Size(min = 1, max = 500, message = "댓글이 너무 길거나 짧습니다. (500자 이내)")
    private String replyContent;
    @Builder
    public ReplySaveDto(String replyContent) {
        this.replyContent = replyContent;
    }
}
