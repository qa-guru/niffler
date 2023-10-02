create extension if not exists "uuid-ossp";

create table if not exists "currency"
(
    id            UUID unique        not null default uuid_generate_v1() primary key,
    currency      varchar(50) unique not null,
    currency_rate float              not null
);

alter table "currency"
    owner to postgres;

delete
from "currency";
insert into "currency"(currency, currency_rate)
values ('RUB', 0.015);
insert into "currency"(currency, currency_rate)
values ('KZT', 0.0021);
insert into "currency"(currency, currency_rate)
values ('EUR', 1.08);
insert into "currency"(currency, currency_rate)
values ('USD', 1.0);
