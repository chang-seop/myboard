package hello.board.myboard.repository;

import hello.board.myboard.domain.Member;
import hello.board.myboard.domain.Authority;
import hello.board.myboard.model.Role;
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
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember).isEqualTo(member);
    }

    @Test
    @DisplayName("email 로 조회 테스트")
    void findByEmail() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findByEmail("hello1@naver.com");

        // then
        assertThat(findMember.orElse(null).getMemberEmail()).isEqualTo("hello1@naver.com");
    }

    @Test
    @DisplayName("권한 저장 2개 테스트")
    void insertListOfAuthority() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        List<Authority> auth = new ArrayList<>();
        auth.add(Authority.builder()
                        .authEmail("hello1@naver.com")
                        .memberId(member.getMemberId())
                        .authRole(Role.ADMIN.getAuth()).build());

        auth.add(Authority.builder()
                        .authEmail("hello1@naver.com")
                        .memberId(member.getMemberId())
                        .authRole(Role.USER.getAuth()).build());

        // when
        memberRepository.insertAuthority(auth);

        // then email 로 조회 후 권한 존재 유무 확인
        Optional<Member> login = memberRepository.findByEmail("hello1@naver.com");
        List<String> roles = login.get().getMemberRoles();

        assertThat(roles.size()).isEqualTo(2);
        assertThat(roles.contains(Role.USER.getAuth())).isTrue();
        assertThat(roles.contains(Role.ADMIN.getAuth())).isTrue();
    }

    @Test
    void findByName() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findByName("한국");

        // then
        assertThat(findMember.orElse(null).getMemberNm()).isEqualTo("한국");
    }
}
