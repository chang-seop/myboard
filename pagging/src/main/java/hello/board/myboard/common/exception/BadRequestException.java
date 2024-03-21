package hello.board.myboard.common.exception;

/**
 * Bad Request 사용자 정의 Exception
 */
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
