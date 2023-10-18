package hello.board.pagging.repository.mybatis;

import hello.board.pagging.domain.File;
import hello.board.pagging.repository.FileRepository;
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
}
