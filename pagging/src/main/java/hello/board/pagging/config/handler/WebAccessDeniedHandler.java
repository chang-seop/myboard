package hello.board.pagging.config.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WebAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 권한이 없는 경우 페이지 이동 (에러 페이지)
        // response.sendRedirect("/error/403");
        // 권한이 없는 경우 에러코드 반환 시 사용
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "403 오류!");
    }
}
