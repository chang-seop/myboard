package hello.board.pagging.model;

import hello.board.pagging.model.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> list = new ArrayList<>();
    private Pagination pagination;

    public PagingResponseDto(List<T> list, Pagination pagination) {
        this.list.addAll(list);
        this.pagination = pagination;
    }
}
