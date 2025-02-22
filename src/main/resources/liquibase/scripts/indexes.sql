-- liquibase formatted sql

--changeset andreiG:1
create index student_name_index on student (name);

--changeset andreiG:2
create index faculty_name_color_index on faculty (name, color);