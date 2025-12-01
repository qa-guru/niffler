create table if not exists "push_tokens" (
  id          UUID unique not null default uuid_generate_v1(),
  user_id     UUID        not null,
  token       varchar unique not null,
  user_agent  varchar,
  is_active   boolean not null default true,
  created_at  date not null default CURRENT_DATE,
  last_seen_at date not null default CURRENT_DATE,
  constraint fk_apush_tokens_users foreign key (user_id) references "user" (id)
);
