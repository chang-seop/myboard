package hello.board.pagging.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
    private int startPosition; // 페이지 번호
    private int maxResult; // 한 페이지당 출력 DATA 개수

    public Criteria() {
        this(1, 10);
    }

    public Criteria(int startPosition, int maxResult) {
        this.startPosition = startPosition;
        this.maxResult = maxResult;
    }

    public int getSkip() {
        return this.startPosition = (startPosition - 1) * maxResult;
    } // 다음 페이지 (startPosition 이동)
}
