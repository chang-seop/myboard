package hello.board.myboard.service;

import hello.board.myboard.common.exception.CustomFileUploadException;
import hello.board.myboard.vo.BoardVo;
import hello.board.myboard.vo.FileVo;
import hello.board.myboard.common.file.FileStore;
import hello.board.myboard.common.file.UploadFile;
import hello.board.myboard.dto.board.BoardModifyDto;
import hello.board.myboard.dto.board.BoardSaveDto;
import hello.board.myboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final FileStore fileStore;

    /**
     * 파일 저장
     * 파일 업로드 언체크 예외 throw
     */
    @Transactional
    public void createFile(BoardSaveDto boardSaveDto, BoardVo boardVo) {
        // 파일 저장
        try {
            List<UploadFile> imageUploadFiles = fileStore.storeFiles(boardSaveDto.getImageFiles());
            if(imageUploadFiles.size() > 0) {
                // 이미지 파일들 저장
                List<FileVo> fileVos = new ArrayList<>();
                for (UploadFile uploadFile : imageUploadFiles) {
                    fileVos.add(FileVo.builder()
                            .boardId(boardVo.getBoardId())
                            .uploadFileName(uploadFile.getUploadFileName())
                            .storeFileName(uploadFile.getStoreFileName())
                            .fileImageYn(true)
                            .build());
                }
                fileRepository.saveAll(fileVos);
            }

            UploadFile attachUploadFile = fileStore.storeFile(boardSaveDto.getAttachFile());
            if(!ObjectUtils.isEmpty(attachUploadFile)) {
                // 첨부 파일 저장
                FileVo fileVo = FileVo.builder()
                        .boardId(boardVo.getBoardId())
                        .uploadFileName(attachUploadFile.getUploadFileName())
                        .storeFileName(attachUploadFile.getStoreFileName())
                        .fileImageYn(false)
                        .build();
                fileRepository.save(fileVo);
            }
        } catch(IOException e) {
            throw new CustomFileUploadException("파일 업로드를 할 수 없습니다."); // 언체크예외 (Transaction)
        }
    }

    /**
     * 파일 수정,
     * 파일 업로드할 수 없음 언체크 예외 throw
     */
    @Transactional
    public List<FileVo> updateFile(BoardModifyDto boardModifyDto, BoardVo boardVo) {
        // 파일 UPDATE
        List<FileVo> dbFileListVo = fileRepository.findByBoardId(boardVo.getBoardId());
        List<Long> modifyFileIdList = boardModifyDto.getFileIdList();
        List<FileVo> deleteedFileListVo = new ArrayList<>();

        if(!ObjectUtils.isEmpty(modifyFileIdList)) {
            // modifyFileIdList 에 값이 있을 경우
            dbFileListVo.forEach(file -> {
                // DB 에 조회한 파일리스트 안에 업로드 했었던 파일이 없을 경우
                if (!modifyFileIdList.contains(file.getFileId())) {
                    // DB 에서 삭제
                    fileRepository.deleteById(file.getFileId());
                    // 스토리지에서 파일 삭제
                    // 트랜잭션이 rollback 되면 파일은 삭제되어버리는 문제 발생! 해결 방안 : 리스트를 컨트롤러에 전달
                    deleteedFileListVo.add(file);
                }
            });
        } else {
            // modifyFileIdList 에 값이 없을 경우 DB 에 있던 파일들 삭제
            dbFileListVo.forEach(file -> {
                // DB 에 조회한 파일리스트 안에 업로드 했었던 파일이 없을 경우 삭제
                fileRepository.deleteById(file.getFileId());
                // 스토리지에서 파일 삭제
                // 트랜잭션이 rollback 되면 파일은 삭제되어버리는 문제 발생!
                // 해결 방안 : 리스트를 BoardService 에 전달
                deleteedFileListVo.add(file);
            });
        }

        // 새로 Upload 한 파일의 데이터를 FILE 테이블에 INSERT
        try {
            List<UploadFile> imageUploadFiles = fileStore.storeFiles(boardModifyDto.getImageFiles());
            if (imageUploadFiles.size() > 0) {
                // 이미지 파일들 저장
                List<FileVo> fileVos = new ArrayList<>();
                for (UploadFile uploadFile : imageUploadFiles) {
                    fileVos.add(FileVo.builder()
                            .boardId(boardVo.getBoardId())
                            .uploadFileName(uploadFile.getUploadFileName())
                            .storeFileName(uploadFile.getStoreFileName())
                            .fileImageYn(true)
                            .build());
                }
                fileRepository.saveAll(fileVos);
            }

            UploadFile attachUploadFile = fileStore.storeFile(boardModifyDto.getAttachFile());
            if (!ObjectUtils.isEmpty(attachUploadFile)) {
                // 첨부 파일 저장
                FileVo fileVo = FileVo.builder()
                        .boardId(boardVo.getBoardId())
                        .uploadFileName(attachUploadFile.getUploadFileName())
                        .storeFileName(attachUploadFile.getStoreFileName())
                        .fileImageYn(false)
                        .build();
                fileRepository.save(fileVo);
            }
        } catch (IOException e) {
            throw new CustomFileUploadException("파일 업로드를 할 수 없습니다."); // 언체크예외 (Transaction)
        }
        return deleteedFileListVo;
    }
}
