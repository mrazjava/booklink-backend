-- used only when spring.jpa.generate-ddl=false (sandbox local)
insert into bl_role values (1, 'ADMIN');
insert into bl_role values (2, 'FOO');
insert into bl_role values (3, 'BAR');
insert into bl_role values (4, 'DETECTIVE');
insert into bl_user_origin (id, origin_name) values (1, 'BOOKLINK');
insert into bl_user_origin (id, origin_name) values (2, 'FACEBOOK');
insert into bl_user_origin (id, origin_name) values (3, 'GOOGLE');
-- password[d0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0] is [abc]
insert into bl_user (id, active, email, f_name, l_name, last_login_on, pwd, auth_token, auth_token_expiry, origin_id) values (1, 1, 'foo@booklink.test', 'Fancy', 'Flamingo', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', null, null, 1);
insert into bl_user (id, active, email, f_name, l_name, last_login_on, pwd, auth_token, auth_token_expiry, origin_id) values (2, 1, 'bar@booklink.test', 'Benevolent', 'Baboon', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', null, null, 1);
insert into bl_user (id, active, email, f_name, l_name, nick_name, last_login_on, pwd, auth_token, auth_token_expiry, origin_id) values (3, 1, 'ant@booklink.test', 'Amazing', 'Ant', 'antsy', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', null, null, 1);
insert into bl_user (id, active, email, f_name, l_name, last_login_on, pwd, auth_token, auth_token_expiry, origin_id) values (4, 1, 'fox@booklink.test', 'Foolish', 'Fox', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', null, null, 1);
insert into bl_user (id, active, email, f_name, l_name, last_login_on, pwd, auth_token, auth_token_expiry, origin_id) values (5, 1, 'dd@booklink.test', 'Delighted', 'Dolphin', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', null, null, 1);
insert into bl_user_role values (1, 2);
insert into bl_user_role values (2, 3);
insert into bl_user_role values (3, 1);
insert into bl_user_role values (5, 4);
alter sequence user_sequence restart 6;