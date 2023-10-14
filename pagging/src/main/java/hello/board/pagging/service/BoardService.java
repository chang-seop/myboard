package hello.board.pagging.service;

import hello.board.pagging.domain.Board;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.board.SearchDto;
import hello.board.pagging.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public List<BoardDto> pageAll(final SearchDto params) {
        List<Board> findAll = boardRepository.findAll(params);
        List<BoardDto> boardDtoList = new ArrayList<>();

        findAll.stream().forEach(board -> boardDtoList.add(BoardDto.builder()
                .boardId(board.getBoardId())
                .memberId(board.getMemberId())
                .boardWriter(board.getBoardWriter())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardRegdate(board.getBoardRegdate())
                .build()));

        return boardDtoList;
    }

    public int findAllCnt() {
        return boardRepository.getPageMaxCount();
    }
}
