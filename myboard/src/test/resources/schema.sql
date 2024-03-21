drop table if exists member cascade;

create table member (
    member_id          bigint not null auto_increment,
    member_nm          varchar(50) not null,
    member_email       varchar(50) not null,
    member_pwd         varchar(100) not null,
    member_regdate     timestamp not null,
    primary key (member_id)
);

drop table if exists member_authority cascade;

create table member_authority(
   authority_id     bigint not null auto_increment,
   member_id        bigint not null,
   auth_email       varchar(50) not null,
   auth_role        varchar(20) not null,
   primary key (authority_id),
   foreign key (member_id) references member(member_id) on delete cascade
);

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
    foreign key (member_id) references member (member_id) on delete cascade
);

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