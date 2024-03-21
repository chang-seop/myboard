package hello.board.myboard.config;

import hello.board.myboard.domain.Board;
import hello.board.myboard.domain.File;
import hello.board.myboard.model.FileStore;
import hello.board.myboard.repository.BoardRepository;
import hello.board.myboard.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileStore fileStore;
    @Bean
    public Job boardJob() {
        Job job = jobBuilderFactory.get("boardJob")
                .start(step())
                .build();
        return job;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("============ boardJob ===========");
                    List<Board> boardList = boardRepository.findDeleteSetup();
                    boardList.forEach((board) -> {
                        // 게시글 삭제 시간 + 7일 (삭제 취소 유효 기간)
                        LocalDateTime deleteTime = board.getBoardDeleteDate().plusDays(7);

                        // deleteTime 보다 현재시간이 과거이면 게시글 삭제 (isBefore : 인자보다 과거일 때 true 리턴)
                        if(deleteTime.isBefore(LocalDateTime.now())) {
                            // 게시글에 관련된 파일 삭제 (스토리지 파일) (댓글과 파일 DB 는 CASCADE 로 연관 삭제가 된다.)
                            List<File> findFile = fileRepository.findByBoardId(board.getBoardId());

                            boolean isRemove = fileStore.removeFiles(findFile);
                            if(!isRemove) {
                                log.error("파일 삭제 실패");
                            }

                            // 게시글 삭제
                            boardRepository.remove(board.getBoardId());
                        }
                    });
                    log.info("============ boardJob ===========");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }
}
