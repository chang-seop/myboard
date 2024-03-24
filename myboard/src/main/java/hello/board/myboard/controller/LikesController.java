package hello.board.myboard.controller;

import hello.board.myboard.dto.likes.LikesBoardDto;
import hello.board.myboard.dto.likes.LikesReplyDto;
import hello.board.myboard.dto.member.MemberDetailsDto;
import hello.board.myboard.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @ResponseBody
    @PostMapping("/board")
    public ResponseEntity<Object> likeBoard(@RequestBody LikesBoardDto likesBoardDto,
                                            @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        int like = likesService.likeBoard(memberDetailsDto.getMemberVo().getMemberId(), likesBoardDto.getBoardId());

        return ResponseEntity.ok(like);
    }

    @ResponseBody
    @PostMapping("/reply")
    public ResponseEntity<Object> likeReply(@RequestBody LikesReplyDto likesReplyDto,
                                            @AuthenticationPrincipal MemberDetailsDto memberDetailsDto) {

        int like = likesService.likeReply(memberDetailsDto.getMemberVo().getMemberId(), likesReplyDto.getReplyId());

        return ResponseEntity.ok(like);
    }
}
