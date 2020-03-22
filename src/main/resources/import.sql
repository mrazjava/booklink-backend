-- used only when spring.jpa.generate-ddl=false (sandbox local)
insert into bl_role values (1, 'ADMIN');
insert into bl_role values (2, 'FOO');
insert into bl_role values (3, 'BAR');
-- user 1 password is [abc]
insert into bl_user values (1, 1, 'foo@bar.com', 'zorro', 'elita', 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', '154288dc-bedc-429a-8cf5-da24487c1431', '2020-04-22T12:03:54.501342+01:00');
insert into bl_user_role values (1, 1);