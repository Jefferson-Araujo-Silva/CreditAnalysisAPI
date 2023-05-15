CREATE TABLE IF NOT EXISTS CLIENT(
        id uuid NOT NULL,
        name varchar(300) NOT NULL,
        cpf varchar(11) UNIQUE NOT NULL,
        monthlyIncome DECIMAL(10, 2) NOT NULL,
        PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CREDITY_ANALYSIS (
  id uuid PRIMARY KEY,
  client_id INT NOT NULL,
  requested_amount DECIMAL(10, 2) NOT NULL,
  approved_value DECIMAL(10, 2) NOT NULL,
  withdrawal_limit_value DECIMAL(10, 2) NOT NULL,
  annual_interest DECIMAL(5, 2) NOT NULL,
  date timestamp
  FOREIGN KEY (client_id) REFERENCES client (id)
);


ALTER TABLE CLIENT
ADD CONSTRAINT fk_credity_analysis_id
FOREIGN KEY (CREDITY_ANALYSIS_id) REFERENCES CREDITY_ANALYSIS (id);