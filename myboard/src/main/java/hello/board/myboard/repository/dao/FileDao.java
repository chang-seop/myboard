package hello.board.myboard.repository.dao;

import hello.board.myboard.vo.FileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileDao {
    void save(FileVo fileVo);
    void saveAll(List<FileVo> fileVos);
    Optional<FileVo> findById(Long fileId);
    List<FileVo> findByBoardId(Long boardId);
    List<FileVo> findByBoardIdAndFileImageYn(@Param("boardId") Long boardId, @Param("fileImageYn") Boolean fileImageYn); // true == 이미지파일, false == 첨부파일
    Optional<FileVo> findByStoreFileName(String storeFileName);
    void deleteById(Long fileId);
    void deleteByStoreFileName(String storeFileName);
}
