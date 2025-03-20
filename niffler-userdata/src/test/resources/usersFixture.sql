INSERT INTO public."user" (id, username, currency, full_name) VALUES ('a9165b45-a4aa-47d6-ac50-43611d624421', 'dima', 'RUB',  'Dmitrii');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('17eb32ff-fad2-41a9-9d22-f6f2a21f6250', 'barsik', 'RUB', 'Barsik');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('2866c33b-69b9-49a4-9176-ccc9b25b54f3', 'bee', 'RUB', 'Bee');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('08716034-690c-4a3f-8961-7b31b45896aa', 'duck', 'RUB','Like a Duck!!!!!');


INSERT INTO public.friendship (requester_id, addressee_id, status, created_date) VALUES ('17eb32ff-fad2-41a9-9d22-f6f2a21f6250', 'a9165b45-a4aa-47d6-ac50-43611d624421', 'PENDING', '2025-01-24');
INSERT INTO public.friendship (requester_id, addressee_id, status, created_date) VALUES ('2866c33b-69b9-49a4-9176-ccc9b25b54f3', '08716034-690c-4a3f-8961-7b31b45896aa', 'PENDING', '2025-03-19');
INSERT INTO public.friendship (requester_id, addressee_id, status, created_date) VALUES ('a9165b45-a4aa-47d6-ac50-43611d624421', '08716034-690c-4a3f-8961-7b31b45896aa', 'ACCEPTED', '2025-01-15');
INSERT INTO public.friendship (requester_id, addressee_id, status, created_date) VALUES ('08716034-690c-4a3f-8961-7b31b45896aa', 'a9165b45-a4aa-47d6-ac50-43611d624421', 'ACCEPTED', '2025-01-15');
