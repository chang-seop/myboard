package hello.board.pagging.controller;

import hello.board.pagging.model.member.MemberDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@AuthenticationPrincipal MemberDetailsDto memberDetailsDto,
                       Model model) {

        if(memberDetailsDto != null) {
            // 로그인 시 회원 닉네임 Model 에 등록
            model.addAttribute("memberName", memberDetailsDto.getMember().getMemberNm() + "님 환영합니다.");
        }

        return "home";
    }
}
