/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  sano
 * Created: Feb 3, 2018
 */
create table users(
username varchar_ignorecase(50) not null primary key,
password varchar_ignorecase(500) not null,
enabled boolean not null);
create table authorities (
username varchar_ignorecase(50) not null,
authority varchar_ignorecase(50) not null,
constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);

create table my_branches (
branchid varchar(50) not null,
branchname varchar(100) not null,
country varchar(50) not null,
constraint pk_my_branch primary key (branchid));

create table my_users (
userid varchar(50) not null,
email varchar(80) not null,
firstname varchar(80) not null,
lastname varchar(80) not null,
constraint pk_my_account primary key (userid));

create table my_signon (
branchid varchar(50) not null,
userid varchar(50)  not null,
foreign key(branchid) references my_branches(branchid),
foreign key(userid) references my_users(userid),
constraint pk_my_signon primary key (branchid, userid));

