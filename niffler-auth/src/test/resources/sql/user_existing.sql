INSERT INTO "user" (id, username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
VALUES (RANDOM_UUID(), 'existinguser', '{noop}password', true, true, true, true);
