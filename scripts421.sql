alter table student add constraint age_constraint CHECK(age >= 16);
alter table student add constraint name unique(name);
alter table faculty add constraint name_and_color unique(name,color);
alter table student alter column age set default 20;
alter table student alter column name set not null;