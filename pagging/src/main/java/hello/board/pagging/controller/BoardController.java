package hello.board.pagging.controller;

import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.board.SearchDto;
import hello.board.pagging.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        List<BoardDto> boardDtoList = boardService.pageAll(params);
        model.addAttribute("boards", boardDtoList);
        return "board";
    }
}
