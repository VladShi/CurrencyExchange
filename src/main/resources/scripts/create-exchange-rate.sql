CREATE TABLE IF NOT EXISTS exchange_rate (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER,
    target_currency_id INTEGER,
    rate NUMERIC(8, 6),
    FOREIGN KEY (base_currency_id) REFERENCES currency(id),
    FOREIGN KEY (target_currency_id) REFERENCES currency(id),
    UNIQUE (base_currency_id, target_currency_id)
);