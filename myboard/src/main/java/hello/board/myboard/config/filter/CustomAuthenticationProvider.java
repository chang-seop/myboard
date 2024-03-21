package hello.board.myboard.config.filter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserDetailsService userDetailsService;
    @NonNull
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // AuthenticationFilter 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String memberEmail = token.getName();
        String memberPwd = (String) token.getCredentials();

        // DB 에서 이메일로 사용자 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(memberEmail);

        // 비밀번호 일치 여부 체크
        if(!passwordEncoder.matches(memberPwd, userDetails.getPassword())) {
            throw new BadCredentialsException(userDetails.getUsername() + "Invalid password");
        }

        // principal(접근대상 정보), credential(비밀번호), authorities(권한 목록)를 token 에 담아 반환
        return new UsernamePasswordAuthenticationToken(userDetails, memberPwd, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
