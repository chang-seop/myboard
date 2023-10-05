drop table if exists board cascade;

create table board (
    board_id     bigint not null auto_increment,
    member_id    bigint not null,
    board_writer       varchar(50) not null,
    board_title        varchar(50) not null,
    board_content      varchar(1000) not null,
    board_regdate      timestamp not null,
    board_update_date   timestamp,
    board_delete_date   timestamp,
    primary key (board_id),
    foreign key (member_id) references member (member_id)
);