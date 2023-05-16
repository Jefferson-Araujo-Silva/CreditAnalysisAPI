CREATE TABLE IF NOT EXISTS CREDITY_ANALYSIS (
  id uuid PRIMARY KEY,
  client_id uuid NOT NULL,
  approved boolean,
  monthlyIncome DECIMAL(10, 2) NOT NULL,
  requested_amount DECIMAL(10, 2) NOT NULL,
  approved_value DECIMAL(10, 2) NOT NULL,
  withdrawal_limit_value DECIMAL(10, 2) NOT NULL,
  annual_interest DECIMAL(5, 2) NOT NULL,
  date timestamp
);