SET @cat_id = random_uuid();
INSERT INTO public.category (id, name, username, archived) VALUES (@cat_id, 'Еда', 'duck', false);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-01', 'RUB', 100.00, 'Завтрак', @cat_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-05', 'RUB', 200.00, 'Обед', @cat_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-10', 'RUB', 300.00, 'Ужин', @cat_id);
