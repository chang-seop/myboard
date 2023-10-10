package hello.board.pagging.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Form Login 에서 로그인에 성공한 경우 동작하기 위해 SavedRequestAwareAuthenticationSuccessHandler 를 상속받는 핸들러를 구현
public class MemberCustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // spring context Holder 에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ...

        // 로그인 후 메인 페이지 이동
        response.sendRedirect("/");
    }
}
