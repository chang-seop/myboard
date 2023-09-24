package hello.board.pagging.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginDto {
    private String email;
    private String psword;

    @Builder
    public MemberLoginDto(String email, String psword) {
        this.email = email;
        this.psword = psword;
    }
}
