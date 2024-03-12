create table model(
    id serial primary key,
    model varchar
);
create table brand(
    id serial primary key,
    brand varchar
);
create table car(
    id serial primary key,
    object_id varchar,
    brand_id int,
    model_id int,
    year int
);
create table category(
    id serial primary key,
    category varchar
);