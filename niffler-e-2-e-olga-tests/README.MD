### Пользователь: olga / 12345

```insert into public.categories (category, username) values ('Рестораны', 'olga');
insert into public.categories (category, username) values ('Продукты', 'olga');
insert into public.categories (category, username) values ('Обучение', 'olga');
insert into public.categories (category, username)values ('Бары', 'olga'); 
insert into public.categories (category, username) values ('Книги', 'olga'); 
```

```
* Create a table friends to the niffler-userdata:
    ```postgresql
    CREATE TABLE friends
    (
    user_id UUID NOT NULL,
    friend_id UUID NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
    );
  ```