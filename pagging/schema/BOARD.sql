drop table if exists board cascade;

create table board (
    board_id     bigint not null auto_increment,
    member_id    bigint not null,
    writer       varchar(50) not null,
    title        varchar(50) not null,
    content      varchar(1000) not null,
    regdate      timestamp not null,
    updatedate   timestamp,
    deletedate   timestamp,
    primary key (board_id),
    foreign key (member_id) references member (member_id)
);