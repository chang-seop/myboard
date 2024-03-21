package hello.board.myboard.repository.mybatis;

import hello.board.myboard.domain.File;
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
    void deleteById(Long fileId);
    void deleteByStoreFileName(String storeFileName);
}
