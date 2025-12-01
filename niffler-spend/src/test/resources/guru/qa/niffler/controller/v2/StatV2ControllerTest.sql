SET @category_id = random_uuid();
INSERT INTO public.category (id, name, username, archived)
VALUES (@category_id, 'Еда', 'duck', false);

SET @archived_category_id = random_uuid();
INSERT INTO public.category (id, name, username, archived)
VALUES (@archived_category_id, 'Другое', 'duck', true);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-05', 'RUB', 1000, 'Завтрак', @category_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-06', 'USD', 50, 'Обед', @category_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-07', 'RUB', 200, 'Книга', @archived_category_id);