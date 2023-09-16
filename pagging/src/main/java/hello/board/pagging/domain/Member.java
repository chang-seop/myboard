package hello.board.pagging.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Member {
    private Long memberId;
    private String name;
    private String email;
    private String psword;
    private LocalDateTime regdate;

    @Builder
    public Member(Long memberId, String name, String email, String psword, LocalDateTime regdate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.psword = psword;
        this.regdate = regdate;
    }
}
