package hello.board.myboard.dto.reply;

import hello.board.myboard.dto.Search;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReplySearchDto extends Search {
    // 객체가 생성되는 시점에 현재 페이지 번호는 1로, 페이지당 출력할 데이터 개수와
    // 하단에 출력할 페이지 개수를 10으로 초기화 합니다.
    public ReplySearchDto() {
        this.setPage(1);
        this.setRecordSize(5);
        this.setPageSize(5);
    }
}
