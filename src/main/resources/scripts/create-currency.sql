CREATE TABLE IF NOT EXISTS currency (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR,
    full_name VARCHAR,
    sign VARCHAR,
    UNIQUE (code)
);