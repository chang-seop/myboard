package hello.board.myboard.controller;

import hello.board.myboard.dto.member.MemberDetailsDto;
import hello.board.myboard.dto.reply.ReplyDeleteDto;
import hello.board.myboard.dto.reply.ReplySaveDto;
import hello.board.myboard.service.BoardService;
import hello.board.myboard.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    private final BoardService boardService;

    /**
     * 댓글 생성
     */
    @PostMapping("/{boardId}")
    public String addReply(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                           @PathVariable("boardId") Long boardId,
                           @ModelAttribute ReplySaveDto replySaveDto,
                           RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("boardId", boardId);
        boolean isNotValid = (replySaveDto.getReplyContent().length() < 1 || replySaveDto.getReplyContent().length() > 500);

        if(isNotValid) {
            return "redirect:/board/{boardId}?replySaveFailure=true";
        }

        replyService.saveReply(replySaveDto, boardId, memberDetailsDto.getMemberVo());
        return "redirect:/board/{boardId}";
    }

    /**
     * 댓글 삭제
     */
    @PostMapping("/delete")
    public String updateReply(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                              @ModelAttribute ReplyDeleteDto replyDeleteDto,
                              RedirectAttributes redirectAttributes) {

        replyService.deleteReply(replyDeleteDto.getReplyId(), replyDeleteDto.getBoardId(), memberDetailsDto.getMemberVo());

        redirectAttributes.addAttribute("boardId", replyDeleteDto.getBoardId());
        return "redirect:/board/{boardId}";
    }
}
