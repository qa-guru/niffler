ALTER TABLE "friendship"
    ADD created_date date NOT NULL DEFAULT CURRENT_DATE;

ALTER TABLE "friendship"
    ALTER COLUMN pending TYPE varchar(50) USING (
        CASE pending
            WHEN TRUE THEN 'PENDING'::varchar(50)
            WHEN FALSE THEN 'ACCEPTED'::varchar(50)
            END
        );

ALTER TABLE "friendship"
    RENAME COLUMN pending TO status;

ALTER TABLE "friendship"
    RENAME COLUMN user_id TO requester_id;

ALTER TABLE "friendship"
    RENAME COLUMN friend_id TO addressee_id;

ALTER TABLE "friendship"
    ADD CONSTRAINT friend_are_distinct_ck CHECK (requester_id <> addressee_id);
