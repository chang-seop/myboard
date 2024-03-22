package hello.board.myboard.repository;

import hello.board.myboard.vo.FileVo;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    void save(FileVo fileVo);
    void saveAll(List<FileVo> fileVos);
    Optional<FileVo> findById(Long fileId);
    List<FileVo> findByBoardId(Long boardId);
    List<FileVo> findByBoardIdAndFileImageYn(Long boardId, Boolean fileImageYn);
    Optional<FileVo> findByStoreFileName(String storeFileName);
    void deleteById(Long fileId);
    void deleteByStoreFileName(String storeFileName);
}
