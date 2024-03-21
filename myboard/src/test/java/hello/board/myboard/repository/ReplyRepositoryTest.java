package hello.board.myboard.repository;

import hello.board.myboard.domain.Board;
import hello.board.myboard.domain.Member;
import hello.board.myboard.domain.Reply;
import hello.board.myboard.model.Pagination;
import hello.board.myboard.model.reply.ReplySearchDto;
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

    private Member initMember() {
        Member member = Member.builder()
                .memberEmail("hello1@naver.com")
                .memberNm("한국")
                .memberPwd("123456")
                .build();
        memberRepository.save(member);
        return member;
    }

    private Board initBoard(Member member) {
        Board board = Board.builder()
                .boardWriter("한국")
                .memberId(member.getMemberId())
                .boardTitle("제목")
                .boardContent("글")
                .build();

        boardRepository.save(board);
        return board;
    }

    @Test
    void save() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        Reply reply = Reply.builder()
                .boardId(board.getBoardId())
                .memberId(member.getMemberId())
                .replyWriter(member.getMemberNm())
                .replyContent("댓글")
                .build();

        // when
        Reply saveReply = replyRepository.save(reply);

        // then
        assertThat(saveReply).isNotNull();
    }

    @Test
    void findById() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        Reply reply = Reply.builder()
                .boardId(board.getBoardId())
                .memberId(member.getMemberId())
                .replyWriter(member.getMemberNm())
                .replyContent("댓글")
                .build();
        Reply saveReply = replyRepository.save(reply);

        // when
        Optional<Reply> replyOptional = replyRepository.findById(reply.getReplyId());

        // then
        assertThat(replyOptional.orElseThrow(() -> new RuntimeException("exception"))).isInstanceOf(Reply.class);
    }

    @Test
    void findByMemberId() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        for (int i = 0; i < 5; i++) {
            Reply reply = Reply.builder()
                    .boardId(board.getBoardId())
                    .memberId(member.getMemberId())
                    .replyWriter(member.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(reply);
        }

        // when
        List<Reply> findReply = replyRepository.findByMemberId(member.getMemberId());

        // then
        assertThat(findReply.size()).isEqualTo(5);
    }

    @Test
    void findByBoardId() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        for (int i = 0; i < 5; i++) {
            Reply reply = Reply.builder()
                    .boardId(board.getBoardId())
                    .memberId(member.getMemberId())
                    .replyWriter(member.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(reply);
        }

        // when
        List<Reply> findReply = replyRepository.findByBoardId(board.getBoardId());

        // then
        assertThat(findReply.size()).isEqualTo(5);
    }

    @Test
    void deleteById() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        Reply reply = Reply.builder()
                .boardId(board.getBoardId())
                .memberId(member.getMemberId())
                .replyWriter(member.getMemberNm())
                .replyContent("댓글")
                .build();
        replyRepository.save(reply);

        // when
        replyRepository.deleteById(reply.getReplyId());

        // then
        Optional<Reply> findReply = replyRepository.findById(reply.getReplyId());
        assertThatThrownBy(() -> findReply.orElseThrow(RuntimeException::new))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void findPageByBoardId() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        for (int i = 0; i < 10; i++) {
            Reply reply = Reply.builder()
                    .boardId(board.getBoardId())
                    .memberId(member.getMemberId())
                    .replyWriter(member.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(reply);
        }

        Integer count = replyRepository.findPageMaxCountByBoardId(board.getBoardId());
        ReplySearchDto replySearchDto = new ReplySearchDto();
        Pagination pagination = new Pagination(count, replySearchDto);
        replySearchDto.setPagination(pagination);

        // when
        List<Reply> findReply = replyRepository.findPageByBoardId(replySearchDto, board.getBoardId());

        // then
        assertThat(findReply.size()).isEqualTo(5); // 페이지네이션 (pageSize 는 5)
    }

    @Test
    void findPageMaxCountByBoardId() {
        // given
        Member member = initMember();
        Board board = initBoard(member);

        for (int i = 0; i < 10; i++) {
            Reply reply = Reply.builder()
                    .boardId(board.getBoardId())
                    .memberId(member.getMemberId())
                    .replyWriter(member.getMemberNm())
                    .replyContent("댓글" + i)
                    .build();
            replyRepository.save(reply);
        }

        // when
        Integer count = replyRepository.findPageMaxCountByBoardId(board.getBoardId());

        // then
        assertThat(count).isEqualTo(10);
    }
}
