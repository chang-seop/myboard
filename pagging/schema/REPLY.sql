drop table if exists reply cascade;

create table reply (
    reply_id     bigint not null auto_increment,
    board_id    bigint not null,
    reply_writer       varchar(50) not null,
    reply_content      varchar(1000) not null,
    reply_regdate      timestamp not null,
    primary key (reply_id),
    foreign key (board_id) references board (board_id) on delete cascade
);