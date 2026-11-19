CREATE TABLE transaction_history (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_id    BIGINT,
    -- Hibernate는 MySQL에서 @Enumerated(STRING)을 ENUM 타입으로 매핑한다
    type          ENUM('WITHDRAW', 'DEPOSIT'),
    amount        DECIMAL(15, 2),
    balance_after DECIMAL(15, 2),
    transacted_at DATETIME(6),
    description   VARCHAR(255),
    CONSTRAINT fk_tx_account FOREIGN KEY (account_id) REFERENCES account (id)
);
CREATE INDEX idx_tx_account_time ON transaction_history (account_id, transacted_at);