package hello.board.myboard.common.exception;

/**
 * 중복 관련 사용자 정의 Exception
 */
public class DuplicateException extends BadRequestException {
    public DuplicateException(String message) {
        super(message);
    }
}
