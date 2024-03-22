package hello.board.myboard.controller;

import hello.board.myboard.common.exception.DuplicateException;
import hello.board.myboard.dto.member.MemberLoginDto;
import hello.board.myboard.dto.member.MemberSaveDto;
import hello.board.myboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    // 로그인 페이지 폼
    @GetMapping("/loginView")
    public String loginForm(@RequestParam(required = false, defaultValue = "false") Boolean fail,
                            @ModelAttribute MemberLoginDto memberLoginDto,
                            Errors errors,
                            Model model) {
        if(fail) {
            // MemberCustomLoginFailHandler 에서 Redirect
            // 로그인 실패 (글로벌 오류 메세지)
            errors.reject("loginFail", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return "login";
    }

    // 회원가입 페이지 폼
    @GetMapping("/signupView")
    public String signupForm(@ModelAttribute MemberSaveDto memberSaveDto) {
        return "signup";
    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberSaveDto memberSaveDto,
                         BindingResult bindingResult,
                         @RequestParam(required = false, defaultValue = "false") Boolean fail) {

        // 바인딩 실패시
        if(bindingResult.hasErrors()) {
            return "signup";
        }

        try{
            memberService.save(memberSaveDto);
        }
        catch(DuplicateException e) {
            // 중복된 이메일 일 경우 (글로벌 오류 메세지)
            bindingResult.reject("duplicate", e.getMessage());
            return "signup";
        }

        return "redirect:/members/loginView";
    }

    // 어드민 페이지 폼
    @GetMapping
    public String adminForm() {
        return "admin";
    }
}
