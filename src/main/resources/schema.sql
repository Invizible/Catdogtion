/* Spring Security tables */
DROP TABLE IF EXISTS users;
create table users (
  username varchar(256),
  password varchar(256),
  enabled boolean
);

DROP TABLE IF EXISTS authorities;
create table authorities (
  username varchar(256),
  authority varchar(256)
);
/* END Spring Security tables */