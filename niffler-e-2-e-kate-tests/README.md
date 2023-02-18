# Tests for niffler app

* User/password: `Kate/pass`
* Spend categories:
    ```postgresql
    insert into categories (category, username) values ('Кафе', 'Kate');
    insert into categories (category, username) values ('Продукты', 'Kate');
    insert into categories (category, username) values ('Обучение', 'Kate');
    insert into categories (category, username) values ('Транспорт', 'Kate');
    ```