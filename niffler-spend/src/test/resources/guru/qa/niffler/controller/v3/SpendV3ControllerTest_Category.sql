SET @food_id = random_uuid();
SET @travel_id = random_uuid();

INSERT INTO public.category (id, name, username, archived) VALUES (@food_id, 'Еда', 'duck', false);
INSERT INTO public.category (id, name, username, archived) VALUES (@travel_id, 'Поездки', 'duck', false);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-01', 'RUB', 100.00, 'Завтрак', @food_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-02', 'RUB', 200.00, 'Обед', @food_id);

INSERT INTO public.spend (id, username, spend_date, currency, amount, description, category_id)
VALUES (random_uuid(), 'duck', '2024-09-03', 'USD', 300.00, 'Такси', @travel_id);
