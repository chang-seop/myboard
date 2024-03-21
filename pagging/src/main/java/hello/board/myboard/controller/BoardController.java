package hello.board.myboard.controller;

import hello.board.myboard.common.exception.BadRequestException;
import hello.board.myboard.model.FileStore;
import hello.board.myboard.model.PagingResponseDto;
import hello.board.myboard.model.board.*;
import hello.board.myboard.model.member.MemberDetailsDto;
import hello.board.myboard.model.reply.ReplyDto;
import hello.board.myboard.model.reply.ReplyDeleteDto;
import hello.board.myboard.model.reply.ReplySaveDto;
import hello.board.myboard.model.reply.ReplySearchDto;
import hello.board.myboard.service.BoardService;
import hello.board.myboard.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;
    private final FileStore fileStore;
    @Value("${file.maxSize}")
    private Integer fileMaxSize;

    /**
     * 게시글 보기 뷰
     */
    @GetMapping()
    public String boardForm(@ModelAttribute("params") final BoardSearchDto params,
                            Model model) {
        PagingResponseDto<BoardDto> response = boardService.findAllBoard(params);
        model.addAttribute("response", response);
        List<SearchType> searchTypeList = new ArrayList<>(Arrays.asList(SearchType.writer, SearchType.content));

        model.addAttribute("searchTypeList", searchTypeList);
        return "board";
    }

    /**
     * 게시글 상세 보기 뷰 (게시글, 파일, 댓글 불러오기)
     */
    @GetMapping("/{boardId}")
    public String boardDetailForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                  @PathVariable("boardId") Long boardId,
                                  @ModelAttribute("params") final ReplySearchDto params,
                                  @RequestParam(value = "replySaveFailure", defaultValue = "false", required = false) Boolean replySaveFailure,
                                  Model model) {
        if(replySaveFailure) {
            model.addAttribute("replySaveFailure", "댓글이 너무 길거나 짧습니다. (1자 이상 500자 이내)");
        }

        BoardDto boardDto = boardService.findBoardAndFiles(boardId);
        PagingResponseDto<ReplyDto> pagingResponseDto = replyService.findPageReply(params, boardId);

        model.addAttribute("replySaveDto", new ReplySaveDto());
        model.addAttribute("replyModifyDto", new ReplyDeleteDto());
        model.addAttribute("authMemberId", memberDetailsDto.getMember().getMemberId()); // 파일 삭제 하기 위한 model 속성
        model.addAttribute("authMemberNm", memberDetailsDto.getMember().getMemberNm());
        model.addAttribute("boardDto", boardDto);
        model.addAttribute("pagingResponseDto", pagingResponseDto);

        return "boardDetail";
    }

    /**
     * 게시글 쓰기 뷰
     */
    @GetMapping("/writeView")
    public String boardWriteFrom(@ModelAttribute BoardSaveDto boardSaveDto,
                                 Model model) {
        model.addAttribute("fileMaxSize", fileMaxSize);
        return "boardWrite";
    }

    /**
     * 게시글 작성 및 파일 업로드
     */
    @PostMapping("/write")
    public String boardWrite(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                             @Valid @ModelAttribute BoardSaveDto boardSaveDto,
                             BindingResult bindingResult,
                             Model model) {

        model.addAttribute("fileMaxSize", fileMaxSize);

        if(bindingResult.hasErrors()) {
            return "boardWrite";
        }

        if(!fileStore.isImageFiles(boardSaveDto.getImageFiles())) {
            // 파일이 존재하면서 이미지 파일 확장자(jpg, png, gif)가 아닌 경우 글로벌 오류 메세지
            bindingResult.reject("isNotImage", "이미지 파일은 jpg, png, gif 만 가능합니다.");
            return "boardWrite";
        }

        if(boardSaveDto.getImageFiles().size() > 3) {
            // 이미지 파일이 3개 이상일 경우 글로벌 오류 메세지
            bindingResult.reject("isManyImage", "이미지 파일은 최대 3개까지 가능합니다.");
            return "boardWrite";
        }

        boardService.createBoard(boardSaveDto, memberDetailsDto.getMember());

        return "redirect:/board";
    }

    /**
     * 게시글 수정 폼,
     * 수정할 수 없는 권한 언체크 예외 throw
     */
    @GetMapping("/modifyView/{boardId}")
    public String boardModifyForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                  @ModelAttribute BoardModifyDto boardModifyDto,
                                  @PathVariable("boardId") Long boardId,
                                  Model model) {

        BoardDto boardAndFiles = boardService.findBoardAndFiles(boardId);

        // 게시글 글쓴이가 아닐 경우 Exception
        if(!boardAndFiles.getMemberId().equals(memberDetailsDto.getMember().getMemberId())) {
            throw new BadRequestException("수정할 수 없는 권한입니다.");
        }

        modifyViewModelAdd(model, boardAndFiles, boardModifyDto);

        return "boardModify";
    }

    /**
     * 게시글 수정
     */
    @PostMapping("/modify")
    public String boardModify(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                              @Valid @ModelAttribute BoardModifyDto boardModifyDto,
                              BindingResult bindingResult,
                              Model model) {

        BoardDto boardAndFiles = boardService.findBoardAndFiles(boardModifyDto.getBoardId());

        if(bindingResult.hasErrors()) {
            modifyViewModelAdd(model, boardAndFiles, boardModifyDto);
            return "boardModify";
        }

        if(!fileStore.isImageFiles(boardModifyDto.getImageFiles())){
            // 파일이 존재하면서 이미지 파일 확장자(jpg, png, gif)가 아닌 경우 글로벌 오류 메세지
            bindingResult.reject("isNotImage", "이미지 파일은 jpg, png, gif 만 가능합니다.");
            modifyViewModelAdd(model, boardAndFiles, boardModifyDto);
            return "boardModify";
        }

        boardService.updateBoard(boardModifyDto, memberDetailsDto.getMember());

        return "redirect:/board";
    }

    private void modifyViewModelAdd( Model model, BoardDto boardAndFiles, BoardModifyDto boardModifyDto) {
        boardModifyDto.setBoardId(boardAndFiles.getBoardId());
        boardModifyDto.setBoardTitle(boardAndFiles.getBoardTitle());
        boardModifyDto.setBoardContent(boardAndFiles.getBoardContent());

        model.addAttribute("fileList", boardAndFiles.getFileList());
        model.addAttribute("fileMaxSize", fileMaxSize);
    }

    /**
     * 게시글 삭제
     */
    @PostMapping("/remove")
    public String boardRemove(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                              @ModelAttribute BoardDeleteDto boardDeleteDto) {
        boardService.deleteSetupBoard(boardDeleteDto.getBoardId(), memberDetailsDto.getMember());

        return "redirect:/board";
    }

    /**
     * 게시글 복구 페이지
     */
    @GetMapping("/recoverView")
    public String boardRecoverForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                   Model model) {
        List<BoardDto> boardDtoList = boardService.findDeleteSetupBoard(memberDetailsDto.getMember().getMemberId());
        model.addAttribute("boardDtoList", boardDtoList);
        return "boardRecover";
    }

    /**
     * 게시글 복구
     */
    @PostMapping("/recover")
    public String boardRecover(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                               @RequestParam("boardId") Long boardId) {

        boardService.updateRecoverBoard(memberDetailsDto.getMember().getMemberId(), boardId);

        return "redirect:/board";
    }
}
