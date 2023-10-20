package hello.board.pagging.service;

import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.domain.File;
import hello.board.pagging.domain.Member;
import hello.board.pagging.model.FileStore;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.UploadFile;
import hello.board.pagging.model.board.BoardDto;
import hello.board.pagging.model.board.BoardSaveDto;
import hello.board.pagging.model.board.PagingResponseDto;
import hello.board.pagging.model.board.SearchDto;
import hello.board.pagging.repository.BoardRepository;
import hello.board.pagging.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final FileStore fileStore;
    @Transactional
    public void createBoard(BoardSaveDto boardSaveDto, Member member) {
        // 게시글 저장
        Board board = Board.builder()
                .memberId(member.getMemberId())
                .boardWriter(member.getMemberNm())
                .boardTitle(boardSaveDto.getBoardTitle())
                .boardContent(boardSaveDto.getBoardContent())
                .boardRegdate(LocalDateTime.now())
                .build();
        boardRepository.save(board);

        // 파일 저장
        try {
            List<UploadFile> imageUploadFiles = fileStore.storeFiles(boardSaveDto.getImageFiles());
            if(imageUploadFiles.size() > 0) {
                // 이미지 파일들 저장
                List<File> files = new ArrayList<>();
                for (UploadFile uploadFile : imageUploadFiles) {
                    files.add(File.builder()
                                    .boardId(board.getBoardId())
                                    .uploadFileName(uploadFile.getUploadFileName())
                                    .storeFileName(uploadFile.getStoreFileName())
                                    .fileImageYn(true)
                                    .fileRegdate(LocalDateTime.now())
                                    .build());
                }
                fileRepository.saveAll(files);
            }

            UploadFile attachUploadFile = fileStore.storeFile(boardSaveDto.getAttachFile());
            if(!ObjectUtils.isEmpty(attachUploadFile)) {
                // 첨부 파일 저장
                File file = File.builder()
                        .boardId(board.getBoardId())
                        .uploadFileName(attachUploadFile.getUploadFileName())
                        .storeFileName(attachUploadFile.getStoreFileName())
                        .fileImageYn(false)
                        .fileRegdate(LocalDateTime.now())
                        .build();
                fileRepository.save(file);
            }
        } catch(IOException e) {
            //TODO: 사용자 정의 예외 만들기
            throw new RuntimeException("파일 업로드를 할 수 없습니다."); // 언체크예외 (Transaction)
        }
    }

    /**
     * findBoard ->
     * @param id
     * @return
     */
    @Transactional
    public BoardDto findBoardAndFiles(Long boardId) {
        // boardId 로 된 File 이 있는지 조회
        Optional<BoardFile> findBoardFile = boardRepository.findByIdWithFile(boardId);
        BoardFile boardFile = findBoardFile.orElse(null);

        if(boardFile != null) {
            // boardFile 이 존재할 경우
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

        // boardFile 이 존재하지 않을 경우 board 조회
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        // TODO: 사용자 정의 함수 만들기
        Board board = boardOptional.orElse(null);
        if(board != null) {
            return BoardDto.builder()
                    .boardId(board.getBoardId())
                    .memberId(board.getMemberId())
                    .boardWriter(board.getBoardWriter())
                    .boardTitle(board.getBoardTitle())
                    .boardContent(board.getBoardContent())
                    .boardRegdate(board.getBoardRegdate())
                    .build();
        }

        return null;
    }

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
