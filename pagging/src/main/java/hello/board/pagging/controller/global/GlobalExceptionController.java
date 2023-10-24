package hello.board.pagging.controller.global;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global Exception Controller / 예외 페이지 반환
 */
@ControllerAdvice
public class GlobalExceptionController {
    @ExceptionHandler(RuntimeException.class)
    String runtimeExceptionHandle(RuntimeException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }
}
