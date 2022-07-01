create table `t_user` (
    `id` bigint unsigned auto_increment,
    `name` varchar(100) not null unique,
    `password` varchar(100) not null,
    `email` varchar(20) not null,
    `create_time` datetime default current_timestamp,
    `update_time` datetime default current_timestamp on update
        current_timestamp,
    primary key pk_id(`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci;


insert into t_user values ('zp', '1234', 'zp@hpe.com');


create table `t_project` (
                          `id` bigint unsigned auto_increment,
                          `name` varchar(100) not null unique,
                          `person_id` bigint not null,
                          `organization` varchar(20) not null,
                          `create_time` datetime default current_timestamp,
                          `update_time` datetime default current_timestamp on update
                              current_timestamp,
                          primary key pk_id(`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_unicode_ci;