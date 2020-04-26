create sequence hibernate_sequence start 1 increment 1;

    create table bl_role (
       id int8 not null,
        role_name varchar(255),
        primary key (id)
    );

    create table bl_user (
       id int8 not null,
        active int4,
        email varchar(255),
        f_name varchar(255),
        last_login_on timestamp,
        l_name varchar(255),
        pwd varchar(255),
        auth_token varchar(255),
        auth_token_expiry timestamp,
        primary key (id)
    );

    create table bl_user_role (
       user_id int8 not null,
        role_id int8 not null,
        primary key (user_id, role_id)
    );

    alter table if exists bl_user 
       add constraint UK_rqw4ym2e457u7mvnkervsdf6t unique (email);

    alter table if exists bl_user 
       add constraint UK_c1hmxwfntwdbqqqk3abcmmit2 unique (auth_token);

    alter table if exists bl_user_role 
       add constraint UK_e0qf1md14dqrhv0vcse9b5c1w unique (role_id);

    alter table if exists bl_user_role 
       add constraint FK4opipv2f26jg6hufwixhjoawo 
       foreign key (role_id) 
       references bl_role;

    alter table if exists bl_user_role 
       add constraint FKw4i7ypt0dri5hfn4b72doypx 
       foreign key (user_id) 
       references bl_user;
insert into bl_role values (1, 'ADMIN');
insert into bl_role values (2, 'FOO');
insert into bl_role values (3, 'BAR');
insert into bl_user ("id", "active", "email", "f_name", "l_name", "last_login_on", "pwd", "auth_token", "auth_token_expiry") values (1, 1, 'foo@bar.com', 'zorro', 'elita', null, 'd0457bd5b5ed542ed1a4a295202d7ed527791071a9acfc202ca850381a2f38ff3224bb37db1136e0', '154288dc-bedc-429a-8cf5-da24487c1431', '2020-04-22T12:03:54.501342+01:00');
insert into bl_user_role values (1, 1);