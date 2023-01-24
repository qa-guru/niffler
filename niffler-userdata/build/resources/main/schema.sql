-- create database "niffler-userdata" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists users
(
    id                      UUID unique        not null default uuid_generate_v1(),
    username                varchar(50) unique not null,
    currency                varchar(3)         not null,
    firstname               varchar(255),
    surname                 varchar(255),
    photo                   bytea,
    primary key (id, username)
);

alter table users
    owner to postgres;
