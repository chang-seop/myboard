package hello.board.pagging.common.exception;

/**
 * 중복 관련 사용자 정의 Exception
 */
public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }
}
