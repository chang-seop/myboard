package hello.board.myboard.repository.mybatis;

import hello.board.myboard.domain.File;
import hello.board.myboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MyBatisFileRepository implements FileRepository {
    private final FileMapper fileMapper;
    @Override
    public void save(File file) {
        fileMapper.save(file);
    }

    @Override
    public void saveAll(List<File> files) {
        fileMapper.saveAll(files);
    }

    @Override
    public Optional<File> findById(Long fileId) {
        return fileMapper.findById(fileId);
    }

    @Override
    public List<File> findByBoardId(Long boardId) {
        return fileMapper.findByBoardId(boardId);
    }

    @Override
    public List<File> findByBoardIdAndFileImageYn(Long boardId, Boolean fileImageYn) {
        return fileMapper.findByBoardIdAndFileImageYn(boardId, fileImageYn);
    }

    @Override
    public Optional<File> findByStoreFileName(String storeFileName) {
        return fileMapper.findByStoreFileName(storeFileName);
    }

    @Override
    public void deleteById(Long fileId) {
        fileMapper.deleteById(fileId);
    }

    @Override
    public void deleteByStoreFileName(String storeFileName) {
        fileMapper.deleteByStoreFileName(storeFileName);
    }
}
