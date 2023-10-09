package hello.board.pagging.service;

import hello.board.pagging.model.member.MemberDetailsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements UserDetailsService {
    private final MemberService memberService;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 사용자 정보가 존재하지 않는 경우 예외 처리
        if(email == null || email.equals("")) {
            return memberService.login(email)
                    .map(u -> new MemberDetailsDto(u, u.getMemberRoles()))
                    .orElseThrow(() -> new AuthenticationServiceException(email));
        }

        // 이메일이 존재하지 않은 경우 예외 처리
        else {
            return memberService.login(email)
                    .map(m -> new MemberDetailsDto(m, m.getMemberRoles()))
                    .orElseThrow(() -> new BadCredentialsException(email));
        }
    }
}
