package hello.board.pagging.repository;

import hello.board.pagging.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Assertions.assertThat(savedMember).isEqualTo(member);
    }

    @Test
    void findByEmail() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findByEmail("hello1@naver.com");

        // then
        Assertions.assertThat(findMember.get().getName()).isEqualTo("한국");
    }
}
