package hello.board.pagging.service;

import hello.board.pagging.domain.Board;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.board.PagingResponseDto;
import hello.board.pagging.model.board.SearchDto;
import hello.board.pagging.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    @Transactional(readOnly = true)
    public PagingResponseDto<BoardDto> findAllBoard(final SearchDto params) {
        // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null 을 담아 반환
        Integer count = boardRepository.getPageMaxCount();

        if (count < 1) {
            return new PagingResponseDto<>(Collections.emptyList(), null);
        }

        // Pagination 객체를 생성해서 페이지 정보 계산 후 SearchDto 타입의 객체인 params 에 계산된 페이지 정보 저장
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        // 계산된 페이지 정보의 일부(limitStart, recordSize)를 기준으로 리스트 데이터 조회 후 응답 데이터 반환
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

        return new PagingResponseDto<>(boardDtoList, pagination);
    }
}
