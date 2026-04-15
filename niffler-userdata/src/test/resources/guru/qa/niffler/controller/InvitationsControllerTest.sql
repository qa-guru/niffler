INSERT INTO public."user" (id, username, currency, full_name) VALUES ('08716034-690c-4a3f-8961-7b31b45896aa', 'duck', 'RUB', 'Like a Duck!!!!!');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('17eb32ff-fad2-41a9-9d22-f6f2a21f6250', 'barsik', 'RUB', 'Barsik');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('2866c33b-69b9-49a4-9176-ccc9b25b54f3', 'bee', 'RUB', 'Bee');

INSERT INTO public.friendship (requester_id, addressee_id, status, created_date) VALUES ('2866c33b-69b9-49a4-9176-ccc9b25b54f3', '08716034-690c-4a3f-8961-7b31b45896aa', 'PENDING', '2025-03-19');
