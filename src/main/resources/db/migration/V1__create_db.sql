
CREATE TABLE IF NOT EXISTS notes
(
    id      UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    title   VARCHAR(200)  NOT NULL CHECK (length(title) > 1),
    content VARCHAR(2000) NOT NULL CHECK (length(title) > 1)
);

