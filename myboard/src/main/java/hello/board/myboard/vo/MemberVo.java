package hello.board.myboard.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberVo {
    private Long memberId;
    private String memberNm;
    private String memberEmail;
    private String memberPwd;
    private LocalDateTime memberRegdate;
    private List<String> memberRoles; // 사용자 권한

    @Builder
    public MemberVo(Long memberId, String memberNm, String memberEmail, String memberPwd, LocalDateTime memberRegdate, List<String> memberRoles) {
        this.memberId = memberId;
        this.memberNm = memberNm;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberRegdate = memberRegdate;
        this.memberRoles = memberRoles;
    }
}
