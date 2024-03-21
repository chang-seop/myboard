package hello.board.myboard.common.exception;

public class CustomFileUploadException extends BadRequestException {
    public CustomFileUploadException(String message) {
        super(message);
    }
}
