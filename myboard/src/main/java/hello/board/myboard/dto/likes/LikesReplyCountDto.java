package hello.board.myboard.dto.likes;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikesReplyCountDto {
    private Long replyId;
    private Integer replyLikeCount;
}
