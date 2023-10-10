package hello.board.pagging.controller;

import hello.board.pagging.model.member.MemberLoginDto;
import hello.board.pagging.model.member.MemberSaveDto;
import hello.board.pagging.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String loginForm(@RequestParam(value = "fail", required = false, defaultValue = "false") Boolean fail,
                            Model model) {
        log.info("login={}", fail);
        if(fail) {
            model.addAttribute("fail", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        model.addAttribute("memberLoginDto", new MemberLoginDto());
        return "login";
    }

    // 회원가입 페이지 폼
    @GetMapping("/signupView")
    public String signupForm(@ModelAttribute("memberSaveDto") MemberSaveDto memberSaveDto) {
        return "signup";
    }

    // 회원가입
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberSaveDto memberSaveDto,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "signup";
        }

        memberService.save(memberSaveDto);

        return "redirect:/members/loginView";
    }

    // 어드민 페이지 폼
    @GetMapping
    public String adminForm() {
        return "admin";
    }
}
