package hello.board.myboard.dto.member.code;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String auth;

    Role(String auth) {
        this.auth = auth;
    }
}
