package hello.board.myboard.repository;

import hello.board.myboard.domain.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    void save(File file);
    void saveAll(List<File> files);
    Optional<File> findById(Long fileId);
    List<File> findByBoardId(Long boardId);
    List<File> findByBoardIdAndFileImageYn(Long boardId, Boolean fileImageYn);
    Optional<File> findByStoreFileName(String storeFileName);
    void deleteById(Long fileId);
    void deleteByStoreFileName(String storeFileName);
}
