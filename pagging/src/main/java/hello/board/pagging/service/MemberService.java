package hello.board.pagging.service;

import hello.board.pagging.common.exception.DuplicateException;
import hello.board.pagging.domain.Authority;
import hello.board.pagging.domain.Member;
import hello.board.pagging.model.Role;
import hello.board.pagging.model.member.MemberSaveDto;
import hello.board.pagging.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Transactional
    public void save(MemberSaveDto memberDto) {
        // email 이 DB 에 중복되는지 확인
        Optional<Member> findMember = memberRepository.findByEmail(memberDto.getMemberEmail());
        if(findMember.isPresent()) {
            throw new DuplicateException("이미 존재하는 이메일");
        }

        // 저장
        Member member = Member.builder()
                .memberNm(memberDto.getMemberNm())
                .memberEmail(memberDto.getMemberEmail())
                .memberPwd(passwordEncoder.encode(memberDto.getMemberPwd())) // 암호화 처리
                .build();

        memberRepository.save(member);

        List<Authority> authList = new ArrayList<>();
        // 유저 권한 등록
        Authority auth = Authority.builder()
                .memberId(member.getMemberId())
                .authEmail(member.getMemberEmail())
                .authRole(Role.USER.getAuth()).build();

        authList.add(auth);

        memberRepository.insertAuthority(authList);
    }

    @Transactional(readOnly = true)
    public Optional<Member> login(String email) {
        return memberRepository.findByEmail(email);
    }
}
