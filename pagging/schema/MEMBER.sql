drop table if exists member cascade;

create table member (
    member_id          bigint not null auto_increment,
    member_nm          varchar(50) not null,
    member_email       varchar(50) not null,
    member_pwd         varchar(100) not null,
    member_regdate     timestamp not null,
    primary key (member_id)
);