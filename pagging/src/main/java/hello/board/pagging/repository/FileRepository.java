package hello.board.pagging.repository;

import hello.board.pagging.domain.BoardFile;
import hello.board.pagging.domain.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {
    void save(File file);
    void saveAll(List<File> files);
    Optional<File> findById(Long fileId);
    List<File> findByBoardId(Long boardId);
}
