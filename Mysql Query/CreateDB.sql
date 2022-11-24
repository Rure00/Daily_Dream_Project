drop database User;
drop database Post;
Create Database User;
use User;

Create table users (
	userName varchar(20) not null unique,
    userPwd varchar(20) not null,
    userEmail varchar(50) not null
);

Create Database Post;
use Post;

Create table posts (
	userName varchar(20) not null,
    title varchar(20) not null,
    content varchar(20) not null,
    likes int default 0
);