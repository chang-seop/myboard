package hello.board.pagging.service;

import hello.board.pagging.common.exception.BadRequestException;
import hello.board.pagging.common.exception.CustomFileUploadException;
import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.domain.File;
import hello.board.pagging.domain.Member;
import hello.board.pagging.model.FileStore;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.PagingResponseDto;
import hello.board.pagging.model.board.*;
import hello.board.pagging.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final FileStore fileStore;

    /**
     * 게시글 저장
     */
    @Transactional
    public void createBoard(BoardSaveDto boardSaveDto, Member member) {
        // 게시글 저장
        Board board = Board.builder()
                .memberId(member.getMemberId())
                .boardWriter(member.getMemberNm())
                .boardTitle(boardSaveDto.getBoardTitle())
                .boardContent(boardSaveDto.getBoardContent())
                .build();
        boardRepository.save(board);

        // 파일 저장 (트랜잭션 참여)
        fileService.createFile(boardSaveDto, board);
    }

    /**
     * BoardFile 조회,
     * 존재하지 않은 게시글, 삭제된 게시글 언체크 예외 throw
     * @return BoardDto
     */
    @Transactional(readOnly = true)
    public BoardDto findBoardAndFiles(Long boardId) {
        // boardId 로 된 File 이 있는지 조회
        Optional<BoardFile> BoardFileOptional = boardRepository.findBoardFileById(boardId);

        // 조회된 데이터가 존재 하지 않을 경우 예외 발생
        BoardFile boardFile = BoardFileOptional.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 게시글이 삭제되어 있는 경우 예외 발생 (boardDeleteDate 가 null 이 아닐 경우)
        if(!ObjectUtils.isEmpty(boardFile.getBoardDeleteDate())) {
            throw new BadRequestException("삭제된 게시글입니다.");
        }

        // boardFile 이 null 이 아닐 경우
        if(!ObjectUtils.isEmpty(boardFile.getFileList())) {
            return BoardDto.builder()
                    .boardId(boardFile.getBoardId())
                    .memberId(boardFile.getMemberId())
                    .boardWriter(boardFile.getBoardWriter())
                    .boardTitle(boardFile.getBoardTitle())
                    .boardContent(boardFile.getBoardContent())
                    .boardRegdate(boardFile.getBoardRegdate())
                    .fileList(boardFile.getFileList())
                    .build();
        }

        // * File 이 존재하지 않은 경우 *
        return BoardDto.builder()
                .boardId(boardFile.getBoardId())
                .memberId(boardFile.getMemberId())
                .boardWriter(boardFile.getBoardWriter())
                .boardTitle(boardFile.getBoardTitle())
                .boardContent(boardFile.getBoardContent())
                .boardRegdate(boardFile.getBoardRegdate())
                .build();
    }

    /**
     * 게시글 모두 조회 (페이징 처리)
     * @return PagingResponseDto<BoardDto>
     */
    @Transactional(readOnly = true)
    public PagingResponseDto<BoardDto> findAllBoard(final BoardSearchDto params) {
        Integer count = boardRepository.getPageMaxCount(params);

        if (count < 1) {
            // 조건에 해당하는 데이터가 없는 경우, 응답 데이터에 비어있는 리스트와 null 을 담아 반환
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

    /**
     * 게시글 삭제 (임시 삭제),
     * 존재하지 않은 게시글, 이미 삭제된 게시글, 삭제할 수 없는 권한 언체크 예외 throw
     */
    @Transactional
    public void deleteSetupBoard(Long boardId, Member member) {
        // 배치를 이용하여 삭제 된지 7일이 지나면 DB 에서도 삭제하게 만들기 위해
        // 현재 이 메서드의 로직은 해당되는 게시글의 board_delete_date 에 삭제 시간을 넣는다.
        Optional<Board> findBoard = boardRepository.findById(boardId);

        // 존재하지 않는 게시글일 경우 Exception
        Board board = findBoard.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 이미 삭제된 게시글일 경우 Exception
        if(!ObjectUtils.isEmpty(board.getBoardDeleteDate())) {
            throw new BadRequestException("이미 삭제된 게시글입니다.");
        }

        // 게시글 글쓴이가 아닐 경우 Exception
        // TODO: ADMIN 권한을 가진 사람은 삭제할 수 있게 한다.
        if(!board.getMemberId().equals(member.getMemberId())) {
            throw new BadRequestException("삭제할 수 없는 권한입니다.");
        }

        boardRepository.deleteSetupByBoardIdAndMemberId(boardId, member.getMemberId());
    }

    /**
     * 게시글 수정,
     * 존재하지 않은 게시글, 이미 삭제된 게시글, 수정할 수 없는 권한, 파일을 삭제하지 못함 언체크 예외 throw
     */
    @Transactional
    public void updateBoard(BoardModifyDto boardModifyDto, Member member) {
        // 존재하지 않은 게시글일 경우 Exception
        Optional<Board> boardOptional = boardRepository.findById(boardModifyDto.getBoardId());
        Board board = boardOptional.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 이미 삭제된 게시글일 경우 Exception
        if (!ObjectUtils.isEmpty(board.getBoardDeleteDate())) {
            throw new BadRequestException("이미 삭제된 게시글입니다.");
        }

        // 게시글 글쓴이가 아닐 경우 Exception
        // TODO: ADMIN 권한을 가진 사람은 수정할 수 있게 한다.
        if (!board.getMemberId().equals(member.getMemberId())) {
            throw new BadRequestException("수정할 수 없는 권한입니다.");
        }

        // Board 테이블에 제목 및 글 내용을 UPDATE
        Board updateBoard = Board.builder()
                .boardId(board.getBoardId()) // boardId 필수!
                .memberId(member.getMemberId())
                .boardTitle(boardModifyDto.getBoardTitle())
                .boardContent(boardModifyDto.getBoardContent())
                .build();
        boardRepository.updateByBoard(updateBoard);


        // 트랜잭션 참여
        List<File> deletedFileList = fileService.updateFile(boardModifyDto, updateBoard);

        if(!fileStore.removeFiles(deletedFileList)) {
            throw new CustomFileUploadException("파일을 삭제하지 못했습니다.");
        }
    }
}
