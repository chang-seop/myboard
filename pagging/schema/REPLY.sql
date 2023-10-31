drop table if exists reply cascade;

create table reply (
    reply_id           bigint not null auto_increment,
    board_id           bigint not null,
    member_id          bigint not null,
    reply_writer       varchar(50) not null,
    reply_content      varchar(500) not null,
    reply_regdate      timestamp not null,
    primary key (reply_id),
    foreign key (board_id) references board (board_id) on delete cascade,
    foreign key (member_id) references member (member_id) on delete cascade
);