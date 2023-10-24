package hello.board.pagging.controller.global;

import hello.board.pagging.common.exception.BadRequestException;
import hello.board.pagging.common.exception.CustomFileUploadException;
import hello.board.pagging.common.exception.DuplicateException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception Controller / 예외 페이지 반환
 */
@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(BadRequestException.class)
    String runtimeExceptionHandle(BadRequestException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(CustomFileUploadException.class)
    String customFileUploadExceptionHandle(CustomFileUploadException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(DuplicateException.class)
    String badRequestExceptionHandle(DuplicateException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }
}
