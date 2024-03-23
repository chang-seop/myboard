package hello.board.myboard.controller;

import hello.board.myboard.vo.FileVo;
import hello.board.myboard.common.file.FileStore;
import hello.board.myboard.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileStore fileStore;
    /**
     * <img> 태그로 이미지를 조회할 때 사용. UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로
     * 이미지 바이너리를 반환한다. 보안에 취약하므로 여러가지 체크 로직을 넣어주는게 좋다.
     */
    @ResponseBody
    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        // DB 에 존재하는지 파일 찾기
        Optional<FileVo> fileOptional = fileRepository.findByStoreFileName(filename);

        if(fileOptional.isPresent()) {
            // DB 에 파일이 존재할 경우
            // 경로에 있는 파일에 접근을 해서 파일을 스트림으로 반환하게 된다.
            UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(filename));

            return ResponseEntity.ok()
                    .cacheControl(CacheControl.maxAge(15, TimeUnit.DAYS)) // 15일 캐시 설정
                    .body(resource);
        }

        response.sendError(404);
        return null;
    }

    /**
     * 파일을 다운로드 할 때 실행한다. 파일 다운로드시에는 고객이 업로드한 파일 이름으로 다운로드 하는게 좋다.
     * 이 때는 content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 주면 된다.
     */
    @GetMapping("/attach/{fileId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long fileId) throws MalformedURLException {
        Optional<FileVo> fileOptional = fileRepository.findById(fileId);
        FileVo fileVo = fileOptional.orElse(null);
        if(fileVo != null) {
            String storeFileName = fileVo.getStoreFileName();
            String uploadFileName = fileVo.getUploadFileName();

            // 사용자가 실제 다운로드 받을 때 업로드 한 파일명이 나와야 한다.
            UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
            log.info("uploadFileName={}", uploadFileName);

            // 헤더에 값을 안넣어 주면 바이너리 그대로 반환 되어 HTML 로 열려진다.
            // 그래서 추가적인 헤더에 값을 넣어줘야 한다.
            String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);// 파일명이 깨질 수 있으므로 인코딩한다.
            String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(urlResource);
        }

        return ResponseEntity.status(404).body(null);
    }
}
