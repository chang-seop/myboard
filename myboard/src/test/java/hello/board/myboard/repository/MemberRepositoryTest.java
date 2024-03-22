package hello.board.myboard.repository;

import hello.board.myboard.vo.MemberVo;
import hello.board.myboard.vo.AuthorityVo;
import hello.board.myboard.dto.member.code.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("유저 저장 테스트")
    void save() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        // when
        MemberVo savedMemberVo = memberRepository.save(memberVo);

        // then
        assertThat(savedMemberVo).isEqualTo(memberVo);
    }

    @Test
    @DisplayName("email 로 조회 테스트")
    void findByEmail() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        // when
        Optional<MemberVo> findMember = memberRepository.findByEmail("hello1@naver.com");

        // then
        assertThat(findMember.orElse(null).getMemberEmail()).isEqualTo("hello1@naver.com");
    }

    @Test
    @DisplayName("권한 저장 2개 테스트")
    void insertListOfAuthority() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        List<AuthorityVo> auth = new ArrayList<>();
        auth.add(AuthorityVo.builder()
                        .authEmail("hello1@naver.com")
                        .memberId(memberVo.getMemberId())
                        .authRole(Role.ADMIN.getAuth()).build());

        auth.add(AuthorityVo.builder()
                        .authEmail("hello1@naver.com")
                        .memberId(memberVo.getMemberId())
                        .authRole(Role.USER.getAuth()).build());

        // when
        memberRepository.insertAuthority(auth);

        // then email 로 조회 후 권한 존재 유무 확인
        Optional<MemberVo> login = memberRepository.findByEmail("hello1@naver.com");
        List<String> roles = login.get().getMemberRoles();

        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles.contains(Role.USER.getAuth())).isTrue();
        assertThat(roles.contains(Role.ADMIN.getAuth())).isTrue();
    }

    @Test
    void findByName() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        // when
        Optional<MemberVo> findMember = memberRepository.findByName("한국");

        // then
        assertThat(findMember.orElse(null).getMemberNm()).isEqualTo("한국");
    }
}
