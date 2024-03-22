package hello.board.myboard.dto.board;

import hello.board.myboard.dto.Search;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardSearchDto extends Search {
    private String keyword; // 검색 키워드
    private String searchType; // 검색 유형

    // 객체가 생성되는 시점에 현재 페이지 번호는 1로, 페이지당 출력할 데이터 개수와
    // 하단에 출력할 페이지 개수를 10으로 초기화 합니다.
    public BoardSearchDto() {
        this.setPage(1);
        this.setRecordSize(10);
        this.setPageSize(10);
    }
}
