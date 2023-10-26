package hello.board.pagging.repository;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.domain.File;
import hello.board.pagging.domain.Member;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.board.SearchDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FileRepository fileRepository;

    @Test
    public void create() {
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

        // when
        Board findBoard = boardRepository.save(board);

        // then
        Assertions.assertThat(findBoard.getMemberId()).isEqualTo(member.getMemberId());
    }

    @Test
    public void findById() {
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

        Board createdBoard = boardRepository.save(board);

        // when
        Optional<Board> findBoard = boardRepository.findById(createdBoard.getBoardId());

        // then
        Assertions.assertThat(findBoard.get().getBoardId()).isEqualTo(createdBoard.getBoardId());
    }

    @Test
    public void findByMemberId() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(member);

        for(int i = 0; i < 2; i++) {
            Board board = Board.builder()
                    .boardWriter("한국")
                    .memberId(member.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(board);
        }

        // when
        List<Board> findBoardList = boardRepository.findByMemberId(member.getMemberId());

        // then
        Assertions.assertThat(findBoardList.get(0).getBoardWriter()).isEqualTo("한국");
        Assertions.assertThat(findBoardList.get(1).getBoardWriter()).isEqualTo("한국");
    }

    @Test
    public void findAll() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(member);

        for(int i = 0; i < 20; i++) {
            Board board = Board.builder()
                    .boardWriter("한국")
                    .memberId(member.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(board);
        }

        SearchDto searchDto = new SearchDto();
        Pagination pagination = new Pagination(boardRepository.getPageMaxCount(), searchDto);
        searchDto.setPagination(pagination);

        // when
        List<Board> findBoardList = boardRepository.findAll(searchDto);

        // then
        Assertions.assertThat(findBoardList.size()).isEqualTo(10);
    }

    @Test
    public void getPageMaxCount() {
        // given
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(member);

        for(int i = 0; i < 20; i++) {
            Board board = Board.builder()
                    .boardWriter("한국")
                    .memberId(member.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(board);
        }

        // when
        Integer maxCount = boardRepository.getPageMaxCount();

        // then
        Assertions.assertThat(maxCount).isEqualTo(20);
    }

    @Test
    void deleteSetupByBoardIdAndMemberId() {
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

        // when
        boardRepository.deleteSetupByBoardIdAndMemberId(board.getBoardId(), member.getMemberId());

        // then
        Optional<Board> findBoard = boardRepository.findById(board.getBoardId());

        Board deleteBoard = findBoard.orElse(null);
        assert deleteBoard != null;
        Assertions.assertThat(deleteBoard.getBoardDeleteDate()).isNotNull();
    }

    @Test
    void updateByBoard() {
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

        Board updateBoard = Board.builder()
                .boardId(board.getBoardId())
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목 변경")
                .boardContent("글 변경")
                .build();

        // when
        boardRepository.updateByBoard(updateBoard);

        // then
        Optional<Board> findBoard = boardRepository.findById(board.getBoardId());
        Board updatedBoard = findBoard.orElse(null);
        assert updatedBoard != null;
        Assertions.assertThat(updatedBoard.getBoardTitle()).isEqualTo("제목 변경");
        Assertions.assertThat(updatedBoard.getBoardContent()).isEqualTo("글 변경");
        Assertions.assertThat(updatedBoard.getBoardUpdateDate()).isNotNull();
    }

    @Test
    public void findByIdWithFile() {
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
        Optional<BoardFile> findBoardFile = boardRepository.findByIdWithFile(board.getBoardId());

        // then
        BoardFile boardFile = findBoardFile.orElse(null);
        Assertions.assertThat(boardFile).isNotNull();
    }
}
