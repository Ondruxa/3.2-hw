create table human(
    id serial primary key,
    name text not null,
    age integer not null,
    driver_license boolean,

);

create table car(
    id integer primary key,
    model varchar,
    brand varchar,
    price integer,
);

create table car_human(
    car_id integer not null references car(id),
    owner_id bigint references human(id)
);