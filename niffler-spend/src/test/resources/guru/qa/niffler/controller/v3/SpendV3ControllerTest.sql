SET @category_id = random_uuid();

INSERT INTO public.category (id, name, username, archived)
VALUES (@category_id, 'Веселье', 'duck', false);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-05', 'RUB', 84550, 'Обучение Niffler Next Generation!', @category_id);