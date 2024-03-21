package hello.board.myboard.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    private Long authorityId;
    private Long memberId;
    private String authEmail;
    private String authRole;

    @Builder
    public Authority(Long authorityId, Long memberId, String authEmail, String authRole) {
        this.authorityId = authorityId;
        this.memberId = memberId;
        this.authEmail = authEmail;
        this.authRole = authRole;
    }
}
