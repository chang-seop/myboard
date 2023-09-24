package hello.board.pagging.service;

import hello.board.pagging.domain.Member;
import hello.board.pagging.dto.MemberLoginDto;
import hello.board.pagging.dto.MemberSaveDto;
import hello.board.pagging.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional(readOnly = true)
    public void save(MemberSaveDto memberDto) {
        Member member = Member.builder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .psword(memberDto.getPsword()) // TODO: 암호화 처리 (인코딩)
                .regdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public void login(MemberLoginDto memberLoginDto) {
        Optional<Member> findMember = memberRepository.findByEmail(memberLoginDto.getEmail());
        Member member = findMember.orElse(null);

        if(member == null) {
            // TODO: ExceptionHandler 이동 (존재하지 않은 이메일)
            throw new RuntimeException();
        }

        // TODO: passwordEncoder.match() 활용
        if(!(member.getPsword().equals(memberLoginDto.getPsword()))) {
            // TODO: ExceptionHandler 이동 (비밀번호 일치하지 않음)
            throw new RuntimeException();
        }
    }
}
