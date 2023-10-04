package hello.board.pagging.model.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberSaveDto {
    private String name;
    private String email;
    private String psword;

    @Builder
    public MemberSaveDto(String name, String email, String psword) {
        this.name = name;
        this.email = email;
        this.psword = psword;
    }
}
