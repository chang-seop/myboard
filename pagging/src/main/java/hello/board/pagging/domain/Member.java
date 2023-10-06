package hello.board.pagging.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    private Long memberId;
    private String memberNm;
    private String memberEmail;
    private String memberPwd;
    private LocalDateTime memberRegdate;
    private List<String> memberRoles; // 사용자 권한

    @Builder
    public Member(Long memberId, String memberNm, String memberEmail, String memberPwd, LocalDateTime memberRegdate, List<String> memberRoles) {
        this.memberId = memberId;
        this.memberNm = memberNm;
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
        this.memberRegdate = memberRegdate;
        this.memberRoles = memberRoles;
    }
}
