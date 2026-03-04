create table users (
                       id UUID PRIMARY KEY,
                       name varchar(120) not null,
                       email varchar(180) not null unique,
                       password_hash varchar(120) not null,
                       role varchar(30) not null,
                       created_at timestamptz not null default now()
);

create index idx_users_email on users(email);