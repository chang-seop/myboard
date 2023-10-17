package hello.board.pagging.controller;

import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.board.PagingResponseDto;
import hello.board.pagging.model.board.SearchDto;
import hello.board.pagging.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    @GetMapping()
    public String boardForm(@ModelAttribute("params") final SearchDto params,
                            Model model) {
        PagingResponseDto<BoardDto> response = boardService.findAllBoard(params);
        model.addAttribute("response", response);
        return "board";
    }

    @GetMapping("/writeView")
    public String boardWriteFrom() {
        return "";
    }

    @PostMapping("/write")
    public String boardWrite() {
        return "";
    }
}
