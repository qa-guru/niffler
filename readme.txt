1. Первое ДЗ

предусловие:
    - создать категорию:
        insert into categories (description) values ('Test');
    - создать пользователя:
        login: qwerty
тест:
    niffler.test.SpendTest.addSpend
    - описание:
            выполнить создание Spend через апи;
            выполнить получение списка Spends через апи;
            найти созданный Spend.
    - условия:
            niffler.jupiter.BeforeSuiteExtension.beforeAllTests - прописываем первоначальные настройки restassured