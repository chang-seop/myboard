package hello.board.myboard.vo;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikesVo {
    private Long likesId;
    private Long memberId;
    private Long boardId;
    private Long replyId;
    private LocalDateTime likesRegdate;

    @Builder
    public LikesVo(Long likesId, Long memberId, Long boardId, Long replyId, LocalDateTime likesRegdate) {
        this.likesId = likesId;
        this.memberId = memberId;
        this.boardId = boardId;
        this.replyId = replyId;
        this.likesRegdate = likesRegdate;
    }
}
