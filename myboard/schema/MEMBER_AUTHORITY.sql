drop table if exists member_authority cascade;

create table member_authority(
   authority_id     bigint not null auto_increment,
   member_id        bigint not null,
   auth_email       varchar(50) not null,
   auth_role        varchar(20) not null,
   primary key (authority_id),
   foreign key (member_id) references member(member_id) on delete cascade
);