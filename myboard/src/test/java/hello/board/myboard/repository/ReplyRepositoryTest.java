package hello.board.myboard.repository;

import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.vo.MemberVo;
import hello.board.myboard.vo.ReplyVo;
import hello.board.myboard.dto.Pagination;
import hello.board.myboard.dto.reply.ReplySearchDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@Transactional
public class ReplyRepositoryTest {
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;

    private MemberVo initMember() {
        MemberVo memberVo = MemberVo.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(memberVo);
        return memberVo;
    }

    private BoardVo initBoard(MemberVo memberVo) {
        BoardVo boardVo = BoardVo.builder()
                .boardWriter("한국")
                .memberId(memberVo.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(boardVo);
        return boardVo;
    }

    @Test
    void save() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        ReplyVo replyVo = ReplyVo.builder()
                .boardId(boardVo.getBoardId())
                .memberId(memberVo.getMemberId())
                .replyWriter(memberVo.getMemberNm())
                .replyContent("댓글")
                .build();

        // when
        ReplyVo saveReplyVo = replyRepository.save(replyVo);

        // then
        assertThat(saveReplyVo).isNotNull();
    }

    @Test
    void findById() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        ReplyVo replyVo = ReplyVo.builder()
                .boardId(boardVo.getBoardId())
                .memberId(memberVo.getMemberId())
                .replyWriter(memberVo.getMemberNm())
                .replyContent("댓글")
                .build();
        ReplyVo saveReplyVo = replyRepository.save(replyVo);

        // when
        Optional<ReplyVo> replyOptional = replyRepository.findById(replyVo.getReplyId());

        // then
        assertThat(replyOptional.orElseThrow(() -> new RuntimeException("exception"))).isInstanceOf(ReplyVo.class);
    }

    @Test
    void findByMemberId() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        for (int i = 0; i < 5; i++) {
            ReplyVo replyVo = ReplyVo.builder()
                    .boardId(boardVo.getBoardId())
                    .memberId(memberVo.getMemberId())
                    .replyWriter(memberVo.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(replyVo);
        }

        // when
        List<ReplyVo> findReplyVo = replyRepository.findByMemberId(memberVo.getMemberId());

        // then
        assertThat(findReplyVo.size()).isEqualTo(5);
    }

    @Test
    void findByBoardId() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        for (int i = 0; i < 5; i++) {
            ReplyVo replyVo = ReplyVo.builder()
                    .boardId(boardVo.getBoardId())
                    .memberId(memberVo.getMemberId())
                    .replyWriter(memberVo.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(replyVo);
        }

        // when
        List<ReplyVo> findReplyVo = replyRepository.findByBoardId(boardVo.getBoardId());

        // then
        assertThat(findReplyVo.size()).isEqualTo(5);
    }

    @Test
    void deleteById() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        ReplyVo replyVo = ReplyVo.builder()
                .boardId(boardVo.getBoardId())
                .memberId(memberVo.getMemberId())
                .replyWriter(memberVo.getMemberNm())
                .replyContent("댓글")
                .build();
        replyRepository.save(replyVo);

        // when
        replyRepository.deleteById(replyVo.getReplyId());

        // then
        Optional<ReplyVo> findReply = replyRepository.findById(replyVo.getReplyId());
        assertThatThrownBy(() -> findReply.orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findPageByBoardId() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        for (int i = 0; i < 10; i++) {
            ReplyVo replyVo = ReplyVo.builder()
                    .boardId(boardVo.getBoardId())
                    .memberId(memberVo.getMemberId())
                    .replyWriter(memberVo.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(replyVo);
        }

        Integer count = replyRepository.findPageMaxCountByBoardId(boardVo.getBoardId());
        ReplySearchDto replySearchDto = new ReplySearchDto();
        Pagination pagination = new Pagination(count, replySearchDto);
        replySearchDto.setPagination(pagination);

        // when
        List<ReplyVo> findReplyVo = replyRepository.findPageByBoardId(replySearchDto, boardVo.getBoardId());

        // then
        assertThat(findReplyVo.size()).isEqualTo(5); // 페이지네이션 (pageSize 는 5)
    }

    @Test
    void findPageMaxCountByBoardId() {
        // given
        MemberVo memberVo = initMember();
        BoardVo boardVo = initBoard(memberVo);

        for (int i = 0; i < 10; i++) {
            ReplyVo replyVo = ReplyVo.builder()
                    .boardId(boardVo.getBoardId())
                    .memberId(memberVo.getMemberId())
                    .replyWriter(memberVo.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(replyVo);
        }

        // when
        Integer count = replyRepository.findPageMaxCountByBoardId(boardVo.getBoardId());

        // then
        assertThat(count).isEqualTo(10);
    }
}
