package hello.board.myboard.vo;

import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorityVo {
    private Long authorityId;
    private Long memberId;
    private String authEmail;
    private String authRole;

    @Builder
    public AuthorityVo(Long authorityId, Long memberId, String authEmail, String authRole) {
        this.authorityId = authorityId;
        this.memberId = memberId;
        this.authEmail = authEmail;
        this.authRole = authRole;
    }
}
