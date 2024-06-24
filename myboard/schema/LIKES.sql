drop table if exists likes cascade;

create table likes (
    likes_id           bigint not null auto_increment,
    member_id           bigint not null,
    board_id          bigint,
    reply_id          bigint,
    likes_regdate      timestamp not null,
    primary key (likes_id),
    foreign key (member_id) references member (member_id) on delete cascade,
    foreign key (board_id) references board (board_id) on delete cascade,
    foreign key (reply_id) references reply (reply_id) on delete cascade
);