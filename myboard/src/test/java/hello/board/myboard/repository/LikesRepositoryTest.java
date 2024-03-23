package hello.board.myboard.repository;

import hello.board.myboard.dto.likes.LikesBoardCountDto;
import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.vo.LikesVo;
import hello.board.myboard.vo.MemberVo;
import hello.board.myboard.vo.ReplyVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikesRepositoryTest {
    @Autowired private LikesRepository likesRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private ReplyRepository replyRepository;

    @BeforeEach
    void init() {
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

        ReplyVo replyVo = ReplyVo.builder()
                .boardId(boardVo.getBoardId())
                .memberId(memberVo.getMemberId())
                .replyWriter(memberVo.getMemberNm())
                .replyContent("댓글")
                .build();
        replyRepository.save(replyVo);

        likesRepository.saveBoardLike(LikesVo.builder()
                .memberId(memberVo.getMemberId())
                .boardId(boardVo.getBoardId())
                .build());

        likesRepository.saveReplyLike(LikesVo.builder()
                .memberId(memberVo.getMemberId())
                .replyId(replyVo.getReplyId())
                .build());
    }
    @Test
    void deleteByMemberIdAndBoardId() {
        // given
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());
        BoardVo boardVo = boardList.get(0);

        // when
        likesRepository.deleteByMemberIdAndBoardId(memberVo.getMemberId(), boardVo.getBoardId());

        // then
        Optional<LikesVo> optionalLikes = likesRepository.findByMemberIdAndBoardId(memberVo.getMemberId(), boardVo.getBoardId());
        assertThat(optionalLikes.orElse(null)).isNull();
    }

    @Test
    void deleteByMemberIdAndReplyId() {
        // given
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());
        BoardVo boardVo = boardList.get(0);
        List<ReplyVo> replyList = replyRepository.findByBoardId(boardVo.getBoardId());
        ReplyVo replyVo = replyList.get(0);

        // when
        likesRepository.deleteByMemberIdAndReplyId(memberVo.getMemberId(), replyVo.getReplyId());

        // then
        Optional<LikesVo> optionalLikes = likesRepository.findByMemberIdAndReplyId(memberVo.getMemberId(), replyVo.getReplyId());
        assertThat(optionalLikes.orElse(null)).isNull();
    }

    @Test
    void findByMemberIdAndBoardId() {
        // given
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());
        BoardVo boardVo = boardList.get(0);

        // when
        Optional<LikesVo> likesOptional = likesRepository.findByMemberIdAndBoardId(memberVo.getMemberId(), boardVo.getBoardId());

        // then
        assertThat(likesOptional.orElse(null)).isNotNull();
    }

    @Test
    void findByMemberIdAndReplyId() {
        // given
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());
        BoardVo boardVo = boardList.get(0);
        List<ReplyVo> replyList = replyRepository.findByBoardId(boardVo.getBoardId());
        ReplyVo replyVo = replyList.get(0);

        // when
        Optional<LikesVo> likesOptional = likesRepository.findByMemberIdAndReplyId(memberVo.getMemberId(), replyVo.getReplyId());

        // then
        assertThat(likesOptional.orElse(null)).isNotNull();
    }

    @Test
    void findCountByBoardId() {
        // given
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());
        BoardVo boardVo = boardList.get(0);

        // when
        Integer count = likesRepository.findCountByBoardId(boardVo.getBoardId());

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void findCountByReplyId() {
        // given
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());
        BoardVo boardVo = boardList.get(0);
        List<ReplyVo> replyList = replyRepository.findByBoardId(boardVo.getBoardId());
        ReplyVo replyVo = replyList.get(0);

        // when
        Integer count = likesRepository.findCountByReplyId(replyVo.getReplyId());

        // then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void findBoardLikeByBoardIdList() {
        MemberVo memberVo = memberRepository.findByName("한국").orElseThrow(RuntimeException::new);
        List<BoardVo> boardList = boardRepository.findByMemberId(memberVo.getMemberId());

        List<Long> boardIdList = boardList.stream()
                .map(BoardVo::getBoardId)
                .collect(Collectors.toList());

        // when
        List<LikesBoardCountDto> list = likesRepository.findBoardLikeByBoardIdList(boardIdList);

        // then
        LikesBoardCountDto likesBoardCountDto = list.get(0);
        assertThat(likesBoardCountDto.getLikeCount()).isEqualTo(1);
    }
}