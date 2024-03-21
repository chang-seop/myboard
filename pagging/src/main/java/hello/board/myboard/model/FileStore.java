package hello.board.myboard.model;

import hello.board.myboard.common.code.ImageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * FileStore - 파일 저장과 관련된 업무 처리
 * 멀티파트 파일을 서버에 저장하는 역할을 담당한다.
 */
@Slf4j
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;


    /**
     * 이미지 파일 .jpeg, .png, .gif 검사 / 파일을 등록하지 않았을 때 통과
     * @param multipartFiles
     * @return true = 이미지 파일, false = 이미지 파일 X
     */
    public boolean isImageFiles(List<MultipartFile> multipartFiles) {
        boolean visited = true;
        if(!ObjectUtils.isEmpty(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                if(multipartFile.isEmpty()) continue; // 파일을 등록하지 않았을 때 통과
                if(!ObjectUtils.isEmpty(multipartFile.getContentType())) {
                    String contentType = multipartFile.getContentType();
                    if(contentType.contains(ImageCode.JPG.getCode())) continue;
                    if(contentType.contains(ImageCode.PNG.getCode())) continue;
                    if(contentType.contains(ImageCode.GIF.getCode())) continue;
                }
                visited = false;
            }
        }
        return visited;
    }

    /**
     * 파일 삭제
     * 파일 삭제를 모두 성공하였을 때 true
     * 파일 삭제가 안되었을 때 false
     */
    public boolean removeFiles(List<hello.board.myboard.domain.File> fileList) {
        for (hello.board.myboard.domain.File file : fileList) {
            File removeFile = new File(getFullPath(file.getStoreFileName()));
            log.info("파일삭제 : {}, {}", file.getFileId(), file.getStoreFileName());
            if (!removeFile.delete()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 파일 저장 경로 반환
     */
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    /**
     * 이미지 파일은 여러개를 업로드가 가능하다
     */
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        if(!ObjectUtils.isEmpty(multipartFiles)) {
            for (MultipartFile multipartFile : multipartFiles) {
                if(!multipartFile.isEmpty()) {
                    storeFileResult.add(storeFile(multipartFile));
                }
            }
        }
        return storeFileResult;
    }

    /**
     * 멀티 파트 파일을 가지고 파일을 저장 후 UploadFile 로 만든 후 반환
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(ObjectUtils.isEmpty(multipartFile)) { // null 체크
            return null;
        }

        if(multipartFile.isEmpty()) { // empty 체크
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = getStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    /**
     * 서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID 를 사용해서 충돌하지 않도록 한다.
     * @param originalFilename
     * @return
     */
    private String getStoreFileName(String originalFilename) {
        // 고객이 입력한 파일 : image.png
        // 서버에 저장하는 파일명 (UUID 를 쓰는데 확장자 명을 남겨두기)
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext; // 저장할 파일 이름 예) 51041c62-86e4-4274-801d-614a7d994edb.png 반환
    }

    /**
     * 확장자를 별도로 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다. 예를 들어서
     * 고객이 a.png 라는 이름으로 업로드하면 51041c62-86e4-4274-801d-614a7d994edb.png 와 같이 저장한다.
     * @param originalFilename
     * @return
     */
    private String extractExt(String originalFilename) { // 예) "png" 반환
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
