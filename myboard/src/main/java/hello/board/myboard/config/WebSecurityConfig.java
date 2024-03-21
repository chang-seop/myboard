package hello.board.myboard.config;

import hello.board.myboard.config.filter.CustomAuthenticationProvider;
import hello.board.myboard.config.filter.MemberCustomAuthenticationFilter;
import hello.board.myboard.config.handler.MemberCustomLoginFailHandler;
import hello.board.myboard.config.handler.MemberCustomLoginSuccessHandler;
import hello.board.myboard.config.handler.WebAccessDeniedHandler;
import hello.board.myboard.config.handler.WebAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final WebAccessDeniedHandler webAccessDeniedHandler;
    private final WebAuthenticationEntryPoint webAuthenticationEntryPoint;

    /**
     * 실제 인증을 담당하는 provider
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(bCryptPasswordEncoder());
    }

    /**
     * 스프링 세큐리티가 사용자를 인증하는 방법이 담긴 객체
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider());
    }

    /**
     * 스프링 세큐리티 룰을 무시할 URL 규칙 설정
     * 정적 자원에 대해서는 Security 설정을 적용하지 않음
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**")
                .antMatchers("/css/**")
                .antMatchers("/vendor/**")
                .antMatchers("/js/**")
                .antMatchers("/favicon/**")
                .antMatchers("/img/**")
                .antMatchers("/error/**");
    }

    /**
     * 스프링 세큐리티 규칙
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // csrf 보안 설정 비활성화
                .antMatcher("/**").authorizeRequests() // 보호된 리소스 URI 에 접근할 수 있는 권한
                .antMatchers("/", "/members/signupView", "/members/signup", "/members/loginView").permitAll() // 전체 접근 허용
                .antMatchers("/board/**").hasAnyRole("ADMIN", "USER") // ROLE_ADMIN 혹은 ROLE_USER 권한을 가진 사용자만 접근 허용
                .antMatchers("/admin/**").hasRole("ADMIN") // ROLE_ADMIN 권한을 가진 사용자만 접근 허용

                // 그 외 항목 전부 인증 적용
                .anyRequest()
                .authenticated()
                .and()

                // exception 처리
                .exceptionHandling()
                .accessDeniedHandler(webAccessDeniedHandler) // 권한이 없는 사용자 접근 시
                .authenticationEntryPoint(webAuthenticationEntryPoint) // 인증되지 않은 사용자 접근 시
                .and()

                .formLogin() // 로그인하는 경우에 대해 설정
                .loginPage("/members/loginView")// 로그인 페이지 URL 설정
                .successForwardUrl("/") // 로그인 성공 시 이동 할 URL 설정
                .failureForwardUrl("/members/loginView") // 로그인 실패 시 이동 할 URL 설정
                .permitAll()
                .and()

                .logout() // 로그아웃 관련 처리
                .logoutUrl("/members/logout") // 로그아웃 URL 설정
                .invalidateHttpSession(true) // 로그아웃 후 세션 초기화 설정
                .deleteCookies("JSESSIONID") // 로그아웃 후 쿠키 삭제 설정
                .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL 설정
                .and()

                // 사용자 인증 필터 적용
                .addFilterBefore(memberCustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 비밀번호 암호화 로직
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * customLoginSuccessHandler 를 CustomAuthenticationFilter 의 인증 성공 핸들러로 추가
     * 로그인 성공 시 /members/login 로그인 url 을 체크하고 인증 토큰 발급
     */
    @Bean
    public MemberCustomAuthenticationFilter memberCustomAuthenticationFilter() throws Exception {
        MemberCustomAuthenticationFilter memberCustomAuthenticationFilter = new MemberCustomAuthenticationFilter(authenticationManager());
        memberCustomAuthenticationFilter.setFilterProcessesUrl("/members/login");
        memberCustomAuthenticationFilter.setAuthenticationSuccessHandler(memberCustomLoginSuccessHandler());
        memberCustomAuthenticationFilter.setAuthenticationFailureHandler(memberCustomLoginFailHandler());
        memberCustomAuthenticationFilter.afterPropertiesSet();
        return memberCustomAuthenticationFilter;
    }

    /**
     * 로그인 성공 시 실행될 handler bean 등록
     */
    @Bean
    public MemberCustomLoginSuccessHandler memberCustomLoginSuccessHandler() {
        return new MemberCustomLoginSuccessHandler();
    }

    /**
     * 로그인 실패 시 실행될 handler bean 등록
     */
    @Bean
    public MemberCustomLoginFailHandler memberCustomLoginFailHandler() {
        return new MemberCustomLoginFailHandler();
    }
}
