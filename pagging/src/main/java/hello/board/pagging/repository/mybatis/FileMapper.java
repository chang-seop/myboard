package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {
    void save(File file);
    void saveAll(List<File> files);
    Optional<File> findById(Long fileId);
    List<File> findByBoardId(Long boardId);
}
