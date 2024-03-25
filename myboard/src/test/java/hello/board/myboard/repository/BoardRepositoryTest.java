package hello.board.myboard.repository;

import hello.board.myboard.vo.*;
import hello.board.myboard.dto.Pagination;
import hello.board.myboard.dto.board.BoardSearchDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void create() {
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

        // when
        BoardVo findBoardVo = boardRepository.save(boardVo);

        // then
        Assertions.assertThat(findBoardVo.getMemberId()).isEqualTo(memberVo.getMemberId());
    }

    @Test
    public void findById() {
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

        BoardVo createdBoardVo = boardRepository.save(boardVo);

        // when
        Optional<BoardVo> findBoard = boardRepository.findById(createdBoardVo.getBoardId());

        // then
        Assertions.assertThat(findBoard.get().getBoardId()).isEqualTo(createdBoardVo.getBoardId());
    }

    @Test
    public void findByMemberId() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);

        for(int i = 0; i < 2; i++) {
            BoardVo boardVo = BoardVo.builder()
                    .boardWriter("한국")
                    .memberId(memberVo.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(boardVo);
        }

        // when
        List<BoardVo> findBoardListVo = boardRepository.findByMemberId(memberVo.getMemberId());

        // then
        Assertions.assertThat(findBoardListVo.get(0).getBoardWriter()).isEqualTo("한국");
        Assertions.assertThat(findBoardListVo.get(1).getBoardWriter()).isEqualTo("한국");
    }

    @Test
    public void findAll() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);

        for(int i = 0; i < 20; i++) {
            BoardVo boardVo = BoardVo.builder()
                    .boardWriter("한국")
                    .memberId(memberVo.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(boardVo);
        }

        BoardSearchDto search = new BoardSearchDto();
        Pagination pagination = new Pagination(boardRepository.getPageMaxCount(search), search);
        search.setPagination(pagination);

        // when
        List<BoardLikesVo> findBoardListVo = boardRepository.findAll(search);

        // then
        Assertions.assertThat(findBoardListVo.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("SearchType = title 로 검색하여 조회하기 테스트")
    public void findAll_SearchType_title() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);

        for(int i = 0; i < 20; i++) {
            BoardVo boardVo = BoardVo.builder()
                    .boardWriter("한국")
                    .memberId(memberVo.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(boardVo);
        }

        BoardSearchDto search = new BoardSearchDto();
        search.setSearchType("title");
        search.setKeyword("제목");

        Pagination pagination = new Pagination(boardRepository.getPageMaxCount(search), search);
        search.setPagination(pagination);

        // when
        List<BoardLikesVo> findBoardListVo = boardRepository.findAll(search);

        // then
        Assertions.assertThat(findBoardListVo.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("SearchType = writer 로 검색하여 조회하기 테스트")
    public void findAll_SearchType_writer() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);

        for(int i = 0; i < 20; i++) {
            BoardVo boardVo = BoardVo.builder()
                    .boardWriter("한국")
                    .memberId(memberVo.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(boardVo);
        }

        BoardSearchDto search = new BoardSearchDto();
        search.setSearchType("writer");
        search.setKeyword("한국");

        Pagination pagination = new Pagination(boardRepository.getPageMaxCount(search), search);
        search.setPagination(pagination);

        // when
        List<BoardLikesVo> findBoardListVo = boardRepository.findAll(search);

        // then
        Assertions.assertThat(findBoardListVo.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("좋아요 순서대로 정렬 테스트")
    public void findAll_likeType() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);

        for(int i = 0; i < 20; i++) {
            BoardVo boardVo = BoardVo.builder()
                    .boardWriter("한국")
                    .memberId(memberVo.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(boardVo);
        }



        BoardSearchDto search = new BoardSearchDto();
        search.setLikeType(true);

        Pagination pagination = new Pagination(boardRepository.getPageMaxCount(search), search);
        search.setPagination(pagination);

        // when
        List<BoardLikesVo> findBoardListVo = boardRepository.findAll(search);

        // then
        Assertions.assertThat(findBoardListVo.size()).isEqualTo(10);
    }

    @Test
    public void getPageMaxCount() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);

        for(int i = 0; i < 20; i++) {
            BoardVo boardVo = BoardVo.builder()
                    .boardWriter("한국")
                    .memberId(memberVo.getMemberId())
                    .boardTitle("제목" + i)
                    .boardContent("글" + i)
                    .build();
            boardRepository.save(boardVo);
        }

        BoardSearchDto search = new BoardSearchDto();
        Pagination pagination = new Pagination(boardRepository.getPageMaxCount(search), search);
        search.setPagination(pagination);

        // when
        Integer maxCount = boardRepository.getPageMaxCount(search);

        // then
        Assertions.assertThat(maxCount).isEqualTo(20);
    }

    @Test
    void deleteSetupByBoardIdAndMemberId() {
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

        // when
        boardRepository.deleteSetupByBoardIdAndMemberId(boardVo.getBoardId(), memberVo.getMemberId());

        // then
        Optional<BoardVo> findBoard = boardRepository.findById(boardVo.getBoardId());

        BoardVo deleteBoardVo = findBoard.orElse(null);
        assert deleteBoardVo != null;
        Assertions.assertThat(deleteBoardVo.getBoardDeleteDate()).isNotNull();
    }

    @Test
    void updateByBoard() {
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

        BoardVo updateBoardVo = BoardVo.builder()
                .boardId(boardVo.getBoardId())
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목 변경")
                .boardContent("글 변경")
                .build();

        // when
        boardRepository.updateByBoard(updateBoardVo);

        // then
        Optional<BoardVo> findBoard = boardRepository.findById(boardVo.getBoardId());
        BoardVo updatedBoardVo = findBoard.orElse(null);
        assert updatedBoardVo != null;
        Assertions.assertThat(updatedBoardVo.getBoardTitle()).isEqualTo("제목 변경");
        Assertions.assertThat(updatedBoardVo.getBoardContent()).isEqualTo("글 변경");
        Assertions.assertThat(updatedBoardVo.getBoardUpdateDate()).isNotNull();
    }

    @Test
    public void findBoardFileReplyById() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter(memberVo.getMemberNm())
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
        Optional<BoardFileVo> findBoardFile = boardRepository.findBoardFileById(boardVo.getBoardId());

        // then
        BoardFileVo boardFileVo = findBoardFile.orElse(null);
        assert boardFileVo != null;
        Assertions.assertThat(boardFileVo.getFileVoList().size()).isEqualTo(5);
    }

    void findDeleteSetup() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter(memberVo.getMemberNm())
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .boardDeleteDate(LocalDateTime.now())
                .build();

        boardRepository.save(boardVo);
        // when
        List<BoardVo> deleteSetup = boardRepository.findDeleteSetup();

        // then
        Assertions.assertThat(deleteSetup.size()).isEqualTo(1);
    }

    @Test
    void remove() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter(memberVo.getMemberNm())
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);

        // when
        boardRepository.remove(boardVo.getBoardId());

        // then
        Optional<BoardVo> findBoard = boardRepository.findById(boardVo.getBoardId());
        Assertions.assertThat(findBoard.orElse(null)).isNull();
    }

    @Test
    void findDeleteSetupByMemberId() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter(memberVo.getMemberNm())
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);
        boardRepository.deleteSetupByBoardIdAndMemberId(boardVo.getBoardId(), memberVo.getMemberId());
        // when
        List<BoardVo> findBoardVo = boardRepository.findDeleteSetupByMemberId(memberVo.getMemberId());

        // then
        Assertions.assertThat(findBoardVo.size()).isEqualTo(1);
    }

    @Test
    void updateRecoverByBoardIdAndMemberId() {
        // given
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();

        memberRepository.save(memberVo);

        BoardVo boardVo = BoardVo.builder()
                .boardWriter(memberVo.getMemberNm())
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);
        boardRepository.deleteSetupByBoardIdAndMemberId(boardVo.getBoardId(), memberVo.getMemberId());

        // when
        int result = boardRepository.updateRecoverByBoardIdAndMemberId(boardVo.getBoardId(), memberVo.getMemberId());

        // then
        List<BoardVo> findBoardVo = boardRepository.findDeleteSetupByMemberId(memberVo.getMemberId());
        Assertions.assertThat(findBoardVo.size()).isEqualTo(0);
        Assertions.assertThat(result).isEqualTo(1);
    }
}
