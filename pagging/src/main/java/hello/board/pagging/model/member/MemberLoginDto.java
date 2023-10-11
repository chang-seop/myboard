package hello.board.pagging.model.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MemberLoginDto {
    private String memberEmail;
    private String memberPwd;

    @Builder
    public MemberLoginDto(String memberEmail, String memberPwd) {
        this.memberEmail = memberEmail;
        this.memberPwd = memberPwd;
    }
}
