drop table if exists file cascade;

create table file (
    file_id     bigint not null auto_increment,
    board_id    bigint not null,
    filename    varchar(100) not null,
    regdate      timestamp not null,
    primary key (file_id),
    foreign key (board_id) references board (board_id)
);