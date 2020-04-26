insert into bl_role values (4, 'DETECTIVE');
insert into bl_user ("id", "active", "email", "f_name", "l_name", "last_login_on", "pwd", "auth_token", "auth_token_expiry") values (5, 1, 'dd@booklink.test', 'Delighted', 'Dolphin', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', null, null);
insert into bl_user_role values (5, 4);
