INSERT INTO public."user" (id, username, currency, full_name) VALUES ('08716034-690c-4a3f-8961-7b31b45896aa', 'duck', 'RUB', 'Duck');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('2866c33b-69b9-49a4-9176-ccc9b25b54f3', 'bee', 'RUB', 'Bee');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('aa000000-0000-0000-0000-000000000001', 'anna', 'RUB', 'Anna');
INSERT INTO public."user" (id, username, currency, full_name) VALUES ('ab000000-0000-0000-0000-000000000001', 'zara', 'RUB', 'Zara');

-- duck sent an invite to bee → bee shows with PENDING status in duck's allUsers list
INSERT INTO public.friendship (requester_id, addressee_id, status, created_date)
VALUES ('08716034-690c-4a3f-8961-7b31b45896aa', '2866c33b-69b9-49a4-9176-ccc9b25b54f3', 'PENDING', '2025-01-01');

-- anna and zara have no relationship with duck (appear as null status in allUsers)
