package hello.board.myboard.service;

import hello.board.myboard.common.exception.DuplicateException;
import hello.board.myboard.vo.AuthorityVo;
import hello.board.myboard.vo.MemberVo;
import hello.board.myboard.dto.member.code.Role;
import hello.board.myboard.dto.member.MemberSaveDto;
import hello.board.myboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // 이메일이 DB 에 중복되는지 확인
        Optional<MemberVo> emailCheckMember = memberRepository.findByEmail(memberDto.getMemberEmail());
        if(emailCheckMember.isPresent()) {
            throw new DuplicateException("이미 존재하는 이메일입니다.");
        }

        // 닉네임이 DB 에 중복되는지 확인
        Optional<MemberVo> nameCheckMember = memberRepository.findByName(memberDto.getMemberNm());
        if(nameCheckMember.isPresent()) {
            throw new DuplicateException("이미 존재하는 닉네임입니다.");
        }

        // 저장
        MemberVo memberVo = MemberVo.builder()
                .memberNm(memberDto.getMemberNm())
                .memberEmail(memberDto.getMemberEmail())
                .memberPwd(passwordEncoder.encode(memberDto.getMemberPwd())) // 암호화 처리
                .build();

        memberRepository.save(memberVo);

        List<AuthorityVo> authList = new ArrayList<>();
        // 유저 권한 등록
        AuthorityVo auth = AuthorityVo.builder()
                .memberId(memberVo.getMemberId())
                .authEmail(memberVo.getMemberEmail())
                .authRole(Role.USER.getAuth()).build();

        authList.add(auth);

        memberRepository.insertAuthority(authList);
    }

    @Transactional(readOnly = true)
    public Optional<MemberVo> login(String email) {
        return memberRepository.findByEmail(email);
    }
}
