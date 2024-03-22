package hello.board.myboard.repository;

import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.vo.FileVo;
import hello.board.myboard.vo.MemberVo;
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
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        FileVo fileVo = FileVo.builder()
                .boardId(boardVo.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();

        // when
        fileRepository.save(fileVo);

        // then
        Optional<FileVo> findFile = fileRepository.findById(fileVo.getFileId());
        assertThat(findFile.orElseThrow(RuntimeException::new)).isInstanceOf(FileVo.class);
    }

    @Test
    void saveAll() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        List<FileVo> fileVos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fileVos.add(FileVo.builder()
                    .boardId(boardVo.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(true)
                    .build());
        }

        // when
        fileRepository.saveAll(fileVos);

        // then
        Optional<FileVo> findFile = fileRepository.findById(fileVos.get(0).getFileId());
        assertThat(findFile.orElseThrow(RuntimeException::new)).isInstanceOf(FileVo.class);
    }

    @Test
    void findByBoardId() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        List<FileVo> fileVos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fileVos.add(FileVo.builder()
                    .boardId(boardVo.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(true)
                    .build());
        }
        fileRepository.saveAll(fileVos);

        // when
        List<FileVo> findBoards = fileRepository.findByBoardId(boardVo.getBoardId());

        // then
        assertThat(findBoards.size()).isEqualTo(5);
    }

    @Test
    void findByBoardIdAndFileImageYn() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        List<FileVo> fileVos = new ArrayList<>();

        // 이미지 파일 5개 INSERT
        for (int i = 0; i < 5; i++) {
            fileVos.add(FileVo.builder()
                    .boardId(boardVo.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(true)
                    .build());
        }

        // 첨부 파일 3개 INSERT
        for (int i = 0; i < 3; i++) {
            fileVos.add(FileVo.builder()
                    .boardId(boardVo.getBoardId())
                    .uploadFileName("hihi" + i)
                    .storeFileName("hehe" + i)
                    .fileImageYn(false)
                    .build());
        }
        fileRepository.saveAll(fileVos);

        // when 이미지 파일 가져오기
        List<FileVo> findFileVo = fileRepository.findByBoardIdAndFileImageYn(boardVo.getBoardId(), true);

        // then
        assertThat(findFileVo.size()).isEqualTo(5); // 기대값 : 5개
    }

    @Test
    void findByStoreFileName() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        FileVo saveFileVo = FileVo.builder()
                .boardId(boardVo.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();
        
        fileRepository.save(saveFileVo);

        // when
        Optional<FileVo> fileOptional = fileRepository.findByStoreFileName(saveFileVo.getStoreFileName());
        FileVo fileVo = fileOptional.orElse(null);

        // then
        assertThat(fileVo).isNotNull();
        assertThat(saveFileVo.getUploadFileName()).isEqualTo(fileVo.getUploadFileName());
    }

    @Test
    void deleteById() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        FileVo saveFileVo = FileVo.builder()
                .boardId(boardVo.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();

        fileRepository.save(saveFileVo);

        // when
        fileRepository.deleteById(saveFileVo.getFileId());

        // then
        Optional<FileVo> findFile = fileRepository.findById(saveFileVo.getFileId());
        assertThat(findFile.isEmpty()).isTrue();
    }

    @Test
    void deleteByStoreFileName() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        FileVo saveFileVo = FileVo.builder()
                .boardId(boardVo.getBoardId())
                .uploadFileName("hihi")
                .storeFileName("hehe")
                .fileImageYn(true)
                .build();

        fileRepository.save(saveFileVo);

        // when
        fileRepository.deleteByStoreFileName("hehe");

        // then
        Optional<FileVo> findFile = fileRepository.findById(saveFileVo.getFileId());
        assertThat(findFile.isEmpty()).isTrue();
    }
}
