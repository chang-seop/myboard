package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {
    void save(File file);
    void saveAll(List<File> files);
    Optional<File> findById(Long fileId);
    List<File> findByBoardId(Long boardId);
    List<File> findByBoardIdAndFileImageYn(@Param("boardId") Long boardId, @Param("fileImageYn") Boolean fileImageYn); // true == 이미지파일, false == 첨부파일
    Optional<File> findByStoreFileName(String storeFileName);
}
