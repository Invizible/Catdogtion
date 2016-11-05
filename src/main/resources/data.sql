-- password: superpassword
insert into users (id, username, first_name, last_name, password, enabled) values (1, 'admin', 'admin', 'admin', '$2a$10$upr/Y2Gv9s5cEWfFmaGwie2mQKVaKHGD1McWiYUz3s5xBccKhd6lS', true);

insert into authorities (id, authority) values (1, 'ROLE_ADMIN');
insert into authorities (id, authority) values (2, 'ROLE_USER');

insert into users_authorities (user_id, authority_id) values (1, 1);
insert into users_authorities (user_id, authority_id) values (1, 2);