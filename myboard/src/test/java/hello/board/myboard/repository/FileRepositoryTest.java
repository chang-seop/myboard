package hello.board.myboard.repository;

import hello.board.myboard.domain.Board;
import hello.board.myboard.domain.File;
import hello.board.myboard.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        File file = File.builder()
                .boardId(board.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
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
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        List<File> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            files.add(File.builder()
                    .boardId(board.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(true)
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
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        List<File> files = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            files.add(File.builder()
                    .boardId(board.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(true)
                    .build());
        }
        fileRepository.saveAll(files);

        // when
        List<File> findBoards = fileRepository.findByBoardId(board.getBoardId());

        // then
        assertThat(findBoards.size()).isEqualTo(5);
    }

    @Test
    void findByBoardIdAndFileImageYn() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        List<File> files = new ArrayList<>();

        // 이미지 파일 5개 INSERT
        for (int i = 0; i < 5; i++) {
            files.add(File.builder()
                    .boardId(board.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(true)
                    .build());
        }

        // 첨부 파일 3개 INSERT
        for (int i = 0; i < 3; i++) {
            files.add(File.builder()
                    .boardId(board.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(false)
                    .build());
        }
        fileRepository.saveAll(files);

        // when 이미지 파일 가져오기
        List<File> findFile = fileRepository.findByBoardIdAndFileImageYn(board.getBoardId(), true);

        // then
        assertThat(findFile.size()).isEqualTo(5); // 기대값 : 5개
    }

    @Test
    void findByStoreFileName() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        File saveFile = File.builder()
                .boardId(board.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();
        
        fileRepository.save(saveFile);

        // when
        Optional<File> fileOptional = fileRepository.findByStoreFileName(saveFile.getStoreFileName());
        File file = fileOptional.orElse(null);

        // then
        assertThat(file).isNotNull();
        assertThat(saveFile.getUploadFileName()).isEqualTo(file.getUploadFileName());
    }

    @Test
    void deleteById() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        File saveFile = File.builder()
                .boardId(board.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();

        fileRepository.save(saveFile);

        // when
        fileRepository.deleteById(saveFile.getFileId());

        // then
        Optional<File> findFile = fileRepository.findById(saveFile.getFileId());
        assertThat(findFile.isEmpty()).isTrue();
    }

    @Test
    void deleteByStoreFileName() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(member);

        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);

        File saveFile = File.builder()
                .boardId(board.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();

        fileRepository.save(saveFile);

        // when
        fileRepository.deleteByStoreFileName("hehe");

        // then
        Optional<File> findFile = fileRepository.findById(saveFile.getFileId());
        assertThat(findFile.isEmpty()).isTrue();
    }
}
