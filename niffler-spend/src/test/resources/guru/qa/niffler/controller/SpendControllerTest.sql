SET @category_id = random_uuid();
INSERT INTO public.category (id, name, username, archived)
VALUES (@category_id, 'Еда', 'duck', false);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES ('5a028ef0-4e26-4ee3-8b5f-6e5c26a9f2e9', 'duck', '2024-10-15', 'RUB', 1500.00, 'Ужин', @category_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-10-16', 'USD', 25.00, 'Кофе', @category_id);
