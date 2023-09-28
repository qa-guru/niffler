-- create database "niffler-userdata" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists "user"
(
    id        UUID unique        not null default uuid_generate_v1(),
    username  varchar(50) unique not null,
    currency  varchar(3)         not null,
    firstname varchar(255),
    surname   varchar(255),
    photo     bytea,
    primary key (id)
);

alter table "user"
    owner to postgres;

create table if not exists "friendship"
(
    user_id   UUID    not null,
    friend_id UUID    not null,
    pending   boolean not null default true,
    primary key (user_id, friend_id),
    constraint fk_user_id foreign key (user_id) references "user" (id),
    constraint fk_friend_id foreign key (friend_id) references "user" (id)
);

alter table "friendship"
    owner to postgres;