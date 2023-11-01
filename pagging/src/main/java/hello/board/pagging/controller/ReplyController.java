package hello.board.pagging.controller;

import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.member.MemberDetailsDto;
import hello.board.pagging.model.reply.ReplyDto;
import hello.board.pagging.model.reply.ReplyDeleteDto;
import hello.board.pagging.model.reply.ReplySaveDto;
import hello.board.pagging.service.BoardService;
import hello.board.pagging.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

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
                           @Valid @ModelAttribute ReplySaveDto replySaveDto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // 필드 에러 발생 시
        if(bindingResult.hasErrors()) {
            List<ReplyDto> replyDtoList = replyService.findReply(boardId);
            BoardDto boardDto = boardService.findBoardAndFiles(boardId);
            model.addAttribute("authMemberId", memberDetailsDto.getMember().getMemberId()); // 파일 삭제 하기 위한 model 속성
            model.addAttribute("authMemberNm", memberDetailsDto.getMember().getMemberNm());
            model.addAttribute("boardDto", boardDto);
            model.addAttribute("replyDtoList", replyDtoList);
            model.addAttribute("replyModifyDto", new ReplyDeleteDto());
            return "boardDetail"; // 렌더링 진행
        }

        replyService.saveReply(replySaveDto, boardId, memberDetailsDto.getMember());

        redirectAttributes.addAttribute("boardId", boardId);
        return "redirect:/board/{boardId}";
    }

    /**
     * 댓글 삭제
     */
    @PostMapping("/delete")
    public String updateReply(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                              @ModelAttribute ReplyDeleteDto replyDeleteDto,
                              RedirectAttributes redirectAttributes) {

        replyService.deleteReply(replyDeleteDto.getReplyId(), replyDeleteDto.getBoardId(), memberDetailsDto.getMember());

        redirectAttributes.addAttribute("boardId", replyDeleteDto.getBoardId());
        return "redirect:/board/{boardId}";
    }
}
