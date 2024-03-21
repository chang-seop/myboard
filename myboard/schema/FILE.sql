drop table if exists file cascade;

create table file (
    file_id     bigint not null auto_increment,
    board_id    bigint not null,
    upload_file_name    varchar(100) not null,
    store_file_name    varchar(100) not null,
    file_image_yn      boolean not null,
    file_regdate      timestamp not null,
    primary key (file_id),
    foreign key (board_id) references board (board_id) on delete cascade
);