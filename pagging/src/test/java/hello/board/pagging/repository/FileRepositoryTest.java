package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.File;
import hello.board.pagging.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class FileRepositoryTest {
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Test
    void save() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .memberRegdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .boardRegdate(LocalDateTime.now())
                .build();

        boardRepository.save(board);

        File file = File.builder()
                .boardId(board.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileRegdate(LocalDateTime.now())
                .build();

        // when
        fileRepository.save(file);

        // then
        Optional<File> findFile = fileRepository.findById(file.getFileId());
        assertThat(findFile.orElseThrow(RuntimeException::new)).isInstanceOf(File.class);
    }

    @Test
    void saveAll() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .memberRegdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .boardRegdate(LocalDateTime.now())
                .build();

        boardRepository.save(board);

        List<File> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            files.add(File.builder()
                    .boardId(board.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileRegdate(LocalDateTime.now())
                    .build());
        }

        // when
        fileRepository.saveAll(files);

        // then
        Optional<File> findFile = fileRepository.findById(files.get(0).getFileId());
        assertThat(findFile.orElseThrow(RuntimeException::new)).isInstanceOf(File.class);
    }

    @Test
    void findByBoardId() {
        // given
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .memberRegdate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .boardRegdate(LocalDateTime.now())
                .build();

        boardRepository.save(board);

        List<File> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            files.add(File.builder()
                    .boardId(board.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileRegdate(LocalDateTime.now())
                    .build());
        }
        fileRepository.saveAll(files);

        // when
        List<File> findBoards = fileRepository.findByBoardId(board.getBoardId());

        // then
        assertThat(findBoards.size()).isEqualTo(5);
    }
}
