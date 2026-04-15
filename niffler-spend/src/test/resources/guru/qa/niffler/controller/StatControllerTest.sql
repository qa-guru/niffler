SET @category_id = random_uuid();
INSERT INTO public.category (id, name, username, archived)
VALUES (@category_id, 'Еда', 'duck', false);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-10-15', 'RUB', 1000.00, 'Обед', @category_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-10-16', 'RUB', 500.00, 'Ужин', @category_id);
