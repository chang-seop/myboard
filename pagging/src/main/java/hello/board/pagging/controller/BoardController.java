package hello.board.pagging.controller;

import hello.board.pagging.common.exception.BadRequestException;
import hello.board.pagging.domain.File;
import hello.board.pagging.model.FileStore;
import hello.board.pagging.model.board.*;
import hello.board.pagging.model.member.MemberDetailsDto;
import hello.board.pagging.repository.FileRepository;
import hello.board.pagging.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final FileStore fileStore;
    private final FileRepository fileRepository;
    @Value("${file.maxSize}")
    private Integer fileMaxSize;

    /**
     * 게시글 보기 뷰
     */
    @GetMapping()
    public String boardForm(@ModelAttribute("params") final SearchDto params,
                            Model model) {
        PagingResponseDto<BoardDto> response = boardService.findAllBoard(params);
        model.addAttribute("response", response);
        return "board";
    }

    /**
     * 게시글 상세 보기 뷰
     */
    @GetMapping("/{boardId}")
    public String boardDetailForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                  @PathVariable("boardId") Long boardId,
                                  Model model) {

        BoardDto boardDto = boardService.findBoardAndFiles(boardId);

        if(boardDto != null) {
            model.addAttribute("AuthMemberId", memberDetailsDto.getMember().getMemberId()); // 삭제 하기 위한 model 속성
            model.addAttribute("boardDto", boardDto);
            return "boardDetail";
        }

        return "redirect:/board";
    }

    /**
     * 게시글 쓰기 뷰
     */
    @GetMapping("/writeView")
    public String boardWriteFrom(@ModelAttribute BoardSaveDto boardSaveDto,
                                 Model model) {
        model.addAttribute("fileMaxSize", fileMaxSize);
        return "boardWrite";
    }

    /**
     * 게시글 작성 및 파일 업로드
     */
    @PostMapping("/write")
    public String boardWrite(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                             @Valid @ModelAttribute BoardSaveDto boardSaveDto,
                             BindingResult bindingResult,
                             Model model) {

        model.addAttribute("fileMaxSize", fileMaxSize);

        if(bindingResult.hasErrors()) {
            return "boardWrite";
        }

        if(!fileStore.isImageFiles(boardSaveDto.getImageFiles())) {
            // 파일이 존재하면서 이미지 파일 확장자(jpg, png, gif)가 아닌 경우 글로벌 오류 메세지
            bindingResult.reject("isNotImage", "이미지 파일은 jpg, png, gif 만 가능합니다.");
            return "boardWrite";
        }

        if(boardSaveDto.getImageFiles().size() > 3) {
            // 이미지 파일이 3개 이상일 경우 글로벌 오류 메세지
            bindingResult.reject("isManyImage", "이미지 파일은 최대 3개까지 가능합니다.");
            return "boardWrite";
        }

        boardService.createBoard(boardSaveDto, memberDetailsDto.getMember());

        return "redirect:/board";
    }

    /**
     * 게시글 수정 폼,
     * 수정할 수 없는 권한 언체크 예외 throw
     */
    @GetMapping("/modifyView/{boardId}")
    public String boardModifyForm(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                                  @ModelAttribute BoardModifyDto boardModifyDto,
                                  @PathVariable("boardId") Long boardId,
                                  Model model) {

        BoardDto boardAndFiles = boardService.findBoardAndFiles(boardId);

        // 게시글 글쓴이가 아닐 경우 Exception
        if(!boardAndFiles.getMemberId().equals(memberDetailsDto.getMember().getMemberId())) {
            throw new BadRequestException("수정할 수 없는 권한입니다.");
        }

        modifyViewModelAdd(model, boardAndFiles, boardModifyDto);

        return "boardModify";
    }

    /**
     * 게시글 수정
     */
    @PostMapping("/modify")
    public String boardModify(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                              @Valid @ModelAttribute BoardModifyDto boardModifyDto,
                              BindingResult bindingResult,
                              Model model) {

        BoardDto boardAndFiles = boardService.findBoardAndFiles(boardModifyDto.getBoardId());

        if(bindingResult.hasErrors()) {
            modifyViewModelAdd(model, boardAndFiles, boardModifyDto);
            return "boardModify";
        }

        if(!fileStore.isImageFiles(boardModifyDto.getImageFiles())){
            // 파일이 존재하면서 이미지 파일 확장자(jpg, png, gif)가 아닌 경우 글로벌 오류 메세지
            bindingResult.reject("isNotImage", "이미지 파일은 jpg, png, gif 만 가능합니다.");
            modifyViewModelAdd(model, boardAndFiles, boardModifyDto);
            return "boardModify";
        }

        // TODO: BoardFileFacade 를 만든 후 책임을 전가하게 리팩토링 한다.
        List<File> deletedFileList = boardService.updateBoard(boardModifyDto, memberDetailsDto.getMember());

        if(!fileStore.removeFiles(deletedFileList)) {
            throw new BadRequestException("파일을 삭제하지 못했습니다.");
        }

        return "redirect:/board";
    }

    private void modifyViewModelAdd( Model model, BoardDto boardAndFiles, BoardModifyDto boardModifyDto) {
        boardModifyDto.setBoardId(boardAndFiles.getBoardId());
        boardModifyDto.setBoardTitle(boardAndFiles.getBoardTitle());
        boardModifyDto.setBoardContent(boardAndFiles.getBoardContent());

        List<File> fileList = null;
        if(!ObjectUtils.isEmpty(boardAndFiles.getFileList())) {
            fileList = boardAndFiles.getFileList();
        }

        model.addAttribute("fileList", fileList);
        model.addAttribute("fileMaxSize", fileMaxSize);
    }

    /**
     * 게시글 삭제
     */
    @PostMapping("/remove")
    public String boardRemove(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                              @ModelAttribute BoardDeleteDto boardDeleteDto) {
        boardService.deleteSetupBoard(boardDeleteDto.getBoardId(), memberDetailsDto.getMember());

        return "redirect:/board";
    }


    /**
     * <img> 태그로 이미지를 조회할 때 사용. UrlResource 로 이미지 파일을 읽어서 @ResponseBody 로
     * 이미지 바이너리를 반환한다. 보안에 취약하므로 여러가지 체크 로직을 넣어주는게 좋다.
     */
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename, HttpServletResponse response) throws IOException {
        // DB 에 존재하는지 파일 찾기
        Optional<File> fileOptional = fileRepository.findByStoreFileName(filename);
        File file = fileOptional.orElse(null);

        if(file != null) {
            // DB 에 파일이 존재할 경우
            // 경로에 있는 파일에 접근을 해서 파일을 스트림으로 반환하게 된다.
            return new UrlResource("file:" + fileStore.getFullPath(filename));
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
        Optional<File> fileOptional = fileRepository.findById(fileId);
        File file = fileOptional.orElse(null);
        if(file != null) {
            String storeFileName = file.getStoreFileName();
            String uploadFileName = file.getUploadFileName();

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
