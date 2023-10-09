package hello.board.pagging.config.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UsernamePasswordAuthenticationFilter 를 상속받아 요청 시 가장 앞단에서 요청을 받는 역할을 하는 Filter 를 구현
 * 요청을 하면 AuthenticationFilter 로 요청이 먼저 들어오게 되고, 사용자가 보낸 아이디와 패스워드를 인터셉트한다.
 * 로그인 시 적절한 유효성 체크를 통과하면 UserPasswordAuthenticationToken 을 발급한다.
 * HttpServletRequest 에서 꺼내온 사용자 아이디와 패스워드를 진짜 인증을 담당할 AuthenticationManager 인터페이스(구현체 - ProviderManager)
 * 에게 인증용 객체(UsernamePasswordAuthenticationToken)로 만들어줘서 위임한다.
 */
public class MemberCustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public MemberCustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getParameter("memberEmail"), request.getParameter("memberPwd"));

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
