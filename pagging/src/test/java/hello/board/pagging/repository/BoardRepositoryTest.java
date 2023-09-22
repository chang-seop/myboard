package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void create() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .writer("한국")
                .memberId(member.getMemberId())
                .title("제목")
                .content("글")
                .regdate(LocalDateTime.now())
                .build();

        // when
        Board findBoard = boardRepository.create(board);

        // then
        Assertions.assertThat(findBoard.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    public void findById() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .writer("한국")
                .memberId(member.getMemberId())
                .title("제목")
                .content("글")
                .regdate(LocalDateTime.now())
                .build();

        Board createdBoard = boardRepository.create(board);

        // when
        Optional<Board> findBoard = boardRepository.findById(createdBoard.getBoardId());

        // then
        Assertions.assertThat(findBoard.get().getBoardId()).isEqualTo(createdBoard.getBoardId());
    }

    @Test
    public void findByMemberId() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        for(int i = 0; i < 2; i++) {
            Board board = Board.builder()
                    .writer("한국")
                    .memberId(member.getMemberId())
                    .title("제목" + i)
                    .content("글" + i)
                    .regdate(LocalDateTime.now())
                    .build();
            boardRepository.create(board);
        }

        // when
        List<Board> findBoardList = boardRepository.findByMemberId(member.getMemberId());

        // then
        Assertions.assertThat(findBoardList.get(0).getWriter()).isEqualTo("한국");
        Assertions.assertThat(findBoardList.get(1).getWriter()).isEqualTo("한국");
    }

    @Test
    public void findAll() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        for(int i = 0; i < 20; i++) {
            Board board = Board.builder()
                    .writer("한국")
                    .memberId(member.getMemberId())
                    .title("제목" + i)
                    .content("글" + i)
                    .regdate(LocalDateTime.now())
                    .build();
            boardRepository.create(board);
        }

        // when
        List<Board> findBoardList = boardRepository.findAll(0, 10);

        // then
        Assertions.assertThat(findBoardList.size()).isEqualTo(10);
    }

    @Test
    public void getMaxCount() {
        // given
        Member member = Member.builder()
                .email("hello1@naver.com")
                .name("한국")
                .psword("123456")
                .regdate(LocalDateTime.now())
                .build();
        memberRepository.save(member);

        for(int i = 0; i < 20; i++) {
            Board board = Board.builder()
                    .writer("한국")
                    .memberId(member.getMemberId())
                    .title("제목" + i)
                    .content("글" + i)
                    .regdate(LocalDateTime.now())
                    .build();
            boardRepository.create(board);
        }

        // when
        Integer maxCount = boardRepository.getMaxCount();

        // then
        Assertions.assertThat(maxCount).isEqualTo(20);
    }
}
