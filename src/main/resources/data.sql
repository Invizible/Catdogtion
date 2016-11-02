insert into users (username, password, enabled) values ('admin', '$2a$10$upr/Y2Gv9s5cEWfFmaGwie2mQKVaKHGD1McWiYUz3s5xBccKhd6lS', true);

insert into authorities (username, authority) values ('admin', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('admin', 'ROLE_USER');