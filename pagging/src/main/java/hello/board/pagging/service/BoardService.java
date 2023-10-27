package hello.board.pagging.service;

import hello.board.pagging.common.exception.BadRequestException;
import hello.board.pagging.common.exception.CustomFileUploadException;
import hello.board.pagging.domain.Board;
import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.domain.File;
import hello.board.pagging.domain.Member;
import hello.board.pagging.model.FileStore;
import hello.board.pagging.model.Pagination;
import hello.board.pagging.model.UploadFile;
import hello.board.pagging.model.board.*;
import hello.board.pagging.repository.BoardRepository;
import hello.board.pagging.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
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

    /**
     * 게시글 저장,
     * 파일 업로드 언체크 예외 throw
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
                        .build();
                fileRepository.save(file);
            }
        } catch(IOException e) {
            throw new CustomFileUploadException("파일 업로드를 할 수 없습니다."); // 언체크예외 (Transaction)
        }
    }

    /**
     * Board 및 BoardFile 조회,
     * 존재하지 않은 게시글, 삭제된 게시글 언체크 예외 throw
     * @return BoardDto
     */
    @Transactional(readOnly = true)
    public BoardDto findBoardAndFiles(Long boardId) {
        // Board 로 조회
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        // 조회된 데이터가 존재 하지 않을 경우 예외 발생
        Board board = boardOptional.orElseThrow(() -> new BadRequestException("존재하지 않은 게시글입니다."));

        // 게시글이 삭제되어 있는 경우 예외 발생 (boardDeleteDate 가 null 이 아닐 경우)
        if(!ObjectUtils.isEmpty(board.getBoardDeleteDate())) {
            throw new BadRequestException("삭제된 게시글입니다.");
        }

        // boardId 로 된 File 이 있는지 조회
        Optional<BoardFile> BoardFileOptional = boardRepository.findByIdWithFile(boardId);
        BoardFile boardFile = BoardFileOptional.orElse(null);

        // boardFile 이 null 이 아닐 경우
        if(boardFile != null) {
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
                .boardId(board.getBoardId())
                .memberId(board.getMemberId())
                .boardWriter(board.getBoardWriter())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardRegdate(board.getBoardRegdate())
                .build();
    }

    /**
     * 게시글 모두 조회 (페이징 처리)
     * @return PagingResponseDto<BoardDto>
     */
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
     * 존재하지 않은 게시글, 이미 삭제된 게시글, 수정할 수 없는 권한, 파일 업로드할 수 없음 언체크 예외 throw
     */
    @Transactional
    public List<File> updateBoard(BoardModifyDto boardModifyDto, Member member) {
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

        // 파일 UPDATE
        List<File> dbFileList = fileRepository.findByBoardId(board.getBoardId());
        List<Long> modifyFileIdList = boardModifyDto.getFileIdList();
        List<File> deleteedFileList = new ArrayList<>();

        if(!ObjectUtils.isEmpty(modifyFileIdList)) {
            // modifyFileIdList 에 값이 있을 경우
            dbFileList.forEach(file -> {
                // DB 에 조회한 파일리스트 안에 업로드 했었던 파일이 없을 경우
                log.info("file.getFileId() : {}", file.getFileId());
                if (!modifyFileIdList.contains(file.getFileId())) {
                    // DB 에서 삭제
                    fileRepository.deleteById(file.getFileId());
                    // 스토리지에서 파일 삭제
                    // 트랜잭션이 rollback 되면 파일은 삭제되어버리는 문제 발생! 해결 방안 : 리스트를 컨트롤러에 전달
                    deleteedFileList.add(file);
                }
            });
        } else {
            // modifyFileIdList 에 값이 없을 경우 DB 에 있던 파일들 삭제
            dbFileList.forEach(file -> {
                // DB 에 조회한 파일리스트 안에 업로드 했었던 파일이 없을 경우
                log.info("file.getFileId() : {}", file.getFileId());
                // DB 에서 삭제
                fileRepository.deleteById(file.getFileId());
                // 스토리지에서 파일 삭제
                // 트랜잭션이 rollback 되면 파일은 삭제되어버리는 문제 발생! 해결 방안 : 리스트를 컨트롤러에 전달
                deleteedFileList.add(file);
            });
        }

        // 새로 Upload 한 파일의 데이터를 FILE 테이블에 INSERT
        try {
            List<UploadFile> imageUploadFiles = fileStore.storeFiles(boardModifyDto.getImageFiles());
            if (imageUploadFiles.size() > 0) {
                // 이미지 파일들 저장
                List<File> files = new ArrayList<>();
                for (UploadFile uploadFile : imageUploadFiles) {
                    files.add(File.builder()
                            .boardId(board.getBoardId())
                            .uploadFileName(uploadFile.getUploadFileName())
                            .storeFileName(uploadFile.getStoreFileName())
                            .fileImageYn(true)
                            .build());
                }
                fileRepository.saveAll(files);
            }

            UploadFile attachUploadFile = fileStore.storeFile(boardModifyDto.getAttachFile());
            if (!ObjectUtils.isEmpty(attachUploadFile)) {
                // 첨부 파일 저장
                File file = File.builder()
                        .boardId(board.getBoardId())
                        .uploadFileName(attachUploadFile.getUploadFileName())
                        .storeFileName(attachUploadFile.getStoreFileName())
                        .fileImageYn(false)
                        .build();
                fileRepository.save(file);
            }
        } catch (IOException e) {
            throw new CustomFileUploadException("파일 업로드를 할 수 없습니다."); // 언체크예외 (Transaction)
        }

        return deleteedFileList;
    }
}
