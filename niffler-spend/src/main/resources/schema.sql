-- create database "niffler-spend" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists "category"
(
    id       UUID unique  not null default uuid_generate_v1() primary key,
    category varchar(255) not null,
    username varchar(50)  not null
);

alter table "category"
    owner to postgres;

create unique index if not exists ix_category_username on "category" (category, username);

create table if not exists "spend"
(
    id          UUID unique  not null default uuid_generate_v1() primary key,
    username    varchar(50)  not null,
    spend_date  date         not null,
    currency    varchar(50)  not null,
    amount      float        not null,
    description varchar(255) not null,
    category_id UUID         not null,
    constraint fk_spend_category foreign key (category_id) references "category" (id)
);

alter table "spend"
    owner to postgres;

-- delete from "category";
-- insert into "category" (category, username) values ('Рестораны', 'dima');
-- insert into "category" (category, username) values ('Продуктовые магазины', 'dima');
-- insert into "category" (category, username) values ('Обучение в QA.GURU ADVANCED', 'dima');

-- insert into "spend" (id, username, spend_date, currency, amount, description, category_id)
-- values (uuid_generate_v1(), 'dima', date('2023-02-15'), 'RUB', 100.0, 'Радостная покупка', (select id from "category" where description = 'Обучение в QA.GURU ADVANCED'));
-- insert into "spend" (id, username, spend_date, currency, amount, description, category_id)
-- values (uuid_generate_v1(), 'dima', date('2023-02-15'), 'RUB', 500.0, 'Радостная покупка', (select id from "category" where description = 'Рестораны'));
-- insert into "spend" (id, username, spend_date, currency, amount, description, category_id)
-- values (uuid_generate_v1(), 'dima', date('2023-02-15'), 'RUB', 10000.0, 'Радостная покупка', (select id from "category" where description = 'Продуктовые магазины'));