SELECT * FROM public.student
ORDER BY id ASC;

SELECT * FROM public.student
where age > 10 and age < 15
ORDER BY id ASC;

SELECT name FROM public.student
ORDER BY id ASC;

SELECT * FROM public.student where name like '%o%' or name like '%O%'
ORDER BY id ASC;

SELECT * FROM public.student
where age < id
ORDER BY id ASC;

SELECT * FROM public.student
ORDER BY age;