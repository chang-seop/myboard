package hello.board.myboard.dto.board.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum SearchType {
    content("제목", "title"),
    writer("글쓴이", "writer"),

    ; // End

    private String name;
    private String value;

    SearchType(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
