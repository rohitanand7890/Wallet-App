CREATE TABLE transaction (
    id SERIAL,
    datetime TIMESTAMP WITH TIME ZONE NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    current_balance DOUBLE PRECISION NOT NULL
);



