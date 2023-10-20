package hello.board.pagging.common.code;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MIME 타입 (IANA 미디어 타입) 정리
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public enum ImageCode {
    JPG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif"),

    ; // End

    private String code;

    ImageCode(String code) {
        this.code = code;
    }
}
