drop table if exists member cascade;

create table member (
    member_id     bigint not null auto_increment,
    name        varchar(50) not null,
    email       varchar(50) not null,
    psword    varchar(100) not null,
    regdate     timestamp not null,
    primary key (member_id)
);