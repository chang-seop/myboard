package hello.board.myboard.repository;

import hello.board.myboard.repository.dao.FileDao;
import hello.board.myboard.vo.FileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisFileRepository implements FileRepository {
    private final FileDao fileDao;
    @Override
    public void save(FileVo fileVo) {
        fileDao.save(fileVo);
    }

    @Override
    public void saveAll(List<FileVo> fileVos) {
        fileDao.saveAll(fileVos);
    }

    @Override
    public Optional<FileVo> findById(Long fileId) {
        return fileDao.findById(fileId);
    }

    @Override
    public List<FileVo> findByBoardId(Long boardId) {
        return fileDao.findByBoardId(boardId);
    }

    @Override
    public List<FileVo> findByBoardIdAndFileImageYn(Long boardId, Boolean fileImageYn) {
        return fileDao.findByBoardIdAndFileImageYn(boardId, fileImageYn);
    }

    @Override
    public Optional<FileVo> findByStoreFileName(String storeFileName) {
        return fileDao.findByStoreFileName(storeFileName);
    }

    @Override
    public void deleteById(Long fileId) {
        fileDao.deleteById(fileId);
    }

    @Override
    public void deleteByStoreFileName(String storeFileName) {
        fileDao.deleteByStoreFileName(storeFileName);
    }
}
