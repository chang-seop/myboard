package hello.board.myboard.service;

import hello.board.myboard.common.exception.BadRequestException;
import hello.board.myboard.common.exception.CustomFileUploadException;
import hello.board.myboard.vo.BoardFileVo;
import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.vo.FileVo;
import hello.board.myboard.vo.MemberVo;
import hello.board.myboard.common.file.FileStore;
import hello.board.myboard.dto.Pagination;
import hello.board.myboard.dto.PagingResponseDto;
import hello.board.myboard.dto.board.*;
import hello.board.myboard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public void createBoard(BoardSaveDto boardSaveDto, MemberVo memberVo) {
        // 게시글 저장
        BoardVo boardVo = BoardVo.builder()
                .memberId(memberVo.getMemberId())
                .boardWriter(memberVo.getMemberNm())
                .boardTitle(boardSaveDto.getBoardTitle())
                .boardContent(boardSaveDto.getBoardContent())
                .build();
        boardRepository.save(boardVo);

        // 파일 저장 (트랜잭션 참여)
        fileService.createFile(boardSaveDto, boardVo);
    }

    /**
     * BoardFile 조회,
     * 존재하지 않은 게시글, 삭제된 게시글 언체크 예외 throw
     * @return BoardDto
     */
    @Transactional(readOnly = true)
    public BoardDto findBoardAndFiles(Long boardId) {
        // boardId 로 된 File 이 있는지 조회
        Optional<BoardFileVo> BoardFileOptional = boardRepository.findBoardFileById(boardId);

        // 조회된 데이터가 존재 하지 않을 경우 예외 발생
        BoardFileVo boardFileVo = BoardFileOptional.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 게시글이 삭제되어 있는 경우 예외 발생 (boardDeleteDate 가 null 이 아닐 경우)
        if(!ObjectUtils.isEmpty(boardFileVo.getBoardDeleteDate())) {
            throw new BadRequestException("삭제된 게시글입니다.");
        }

        // boardFile 이 null 이 아닐 경우
        if(!ObjectUtils.isEmpty(boardFileVo.getFileVoList())) {
            return BoardDto.builder()
                    .boardId(boardFileVo.getBoardId())
                    .memberId(boardFileVo.getMemberId())
                    .boardWriter(boardFileVo.getBoardWriter())
                    .boardTitle(boardFileVo.getBoardTitle())
                    .boardContent(boardFileVo.getBoardContent())
                    .boardRegdate(boardFileVo.getBoardRegdate())
                    .fileList(boardFileVo.getFileVoList())
                    .build();
        }

        // * File 이 존재하지 않은 경우 *
        return BoardDto.builder()
                .boardId(boardFileVo.getBoardId())
                .memberId(boardFileVo.getMemberId())
                .boardWriter(boardFileVo.getBoardWriter())
                .boardTitle(boardFileVo.getBoardTitle())
                .boardContent(boardFileVo.getBoardContent())
                .boardRegdate(boardFileVo.getBoardRegdate())
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
        List<BoardVo> findAll = boardRepository.findAll(params);

        List<BoardDto> boardDtoList = findAll
                .stream()
                .map(BoardVo::toDto)
                .collect(Collectors.toList());

        return new PagingResponseDto<>(boardDtoList, pagination);
    }

    /**
     * 게시글 삭제 (임시 삭제),
     * 존재하지 않은 게시글, 이미 삭제된 게시글, 삭제할 수 없는 권한 언체크 예외 throw
     */
    @Transactional
    public void deleteSetupBoard(Long boardId, MemberVo memberVo) {
        // 배치를 이용하여 삭제 된지 7일이 지나면 DB 에서도 삭제하게 만들기 위해
        // 현재 이 메서드의 로직은 해당되는 게시글의 board_delete_date 에 삭제 시간을 넣는다.
        Optional<BoardVo> findBoard = boardRepository.findById(boardId);

        // 존재하지 않는 게시글일 경우 Exception
        BoardVo boardVo = findBoard.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 이미 삭제된 게시글일 경우 Exception
        if(!ObjectUtils.isEmpty(boardVo.getBoardDeleteDate())) {
            throw new BadRequestException("이미 삭제된 게시글입니다.");
        }

        // 게시글 글쓴이가 아닐 경우 Exception
        // TODO: ADMIN 권한을 가진 사람은 삭제할 수 있게 한다.
        if(!boardVo.getMemberId().equals(memberVo.getMemberId())) {
            throw new BadRequestException("삭제할 수 없는 권한입니다.");
        }

        boardRepository.deleteSetupByBoardIdAndMemberId(boardId, memberVo.getMemberId());
    }

    /**
     * 게시글 수정,
     * 존재하지 않은 게시글, 이미 삭제된 게시글, 수정할 수 없는 권한, 파일을 삭제하지 못함 언체크 예외 throw
     */
    @Transactional
    public void updateBoard(BoardModifyDto boardModifyDto, MemberVo memberVo) {
        // 존재하지 않은 게시글일 경우 Exception
        Optional<BoardVo> boardOptional = boardRepository.findById(boardModifyDto.getBoardId());
        BoardVo boardVo = boardOptional.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 이미 삭제된 게시글일 경우 Exception
        if (!ObjectUtils.isEmpty(boardVo.getBoardDeleteDate())) {
            throw new BadRequestException("이미 삭제된 게시글입니다.");
        }

        // 게시글 글쓴이가 아닐 경우 Exception
        // TODO: ADMIN 권한을 가진 사람은 수정할 수 있게 한다.
        if (!boardVo.getMemberId().equals(memberVo.getMemberId())) {
            throw new BadRequestException("수정할 수 없는 권한입니다.");
        }

        // Board 테이블에 제목 및 글 내용을 UPDATE
        BoardVo updateBoardVo = BoardVo.builder()
                .boardId(boardVo.getBoardId()) // boardId 필수!
                .memberId(memberVo.getMemberId())
                .boardTitle(boardModifyDto.getBoardTitle())
                .boardContent(boardModifyDto.getBoardContent())
                .build();
        boardRepository.updateByBoard(updateBoardVo);


        // 트랜잭션 참여
        List<FileVo> deletedFileListVo = fileService.updateFile(boardModifyDto, updateBoardVo);

        if(!fileStore.removeFiles(deletedFileListVo)) {
            throw new CustomFileUploadException("파일을 삭제하지 못했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<BoardDto> findDeleteSetupBoard(Long memberId) {
        List<BoardVo> findBoardVo = boardRepository.findDeleteSetupByMemberId(memberId);

        return findBoardVo.stream()
                .map(BoardVo::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRecoverBoard(Long memberId, Long boardId) {
        // 게시글이 있는지 확인
        BoardVo findBoardVo = boardRepository.findById(boardId)
                .orElseThrow(() -> new BadRequestException("게시글이 존재하지 않습니다."));

        // 사용자의 게시글인지 확인
        if(!findBoardVo.getMemberId().equals(memberId)) {
            throw new BadRequestException("사용자의 게시글이 아닙니다.");
        }

        int result = boardRepository.updateRecoverByBoardIdAndMemberId(boardId, memberId);

        if(result == 0) {
            // 업데이트 실패
            throw new BadRequestException("이미 복구된 게시글 또는 게시글을 복구할 수 없습니다.");
        }
    }
}
