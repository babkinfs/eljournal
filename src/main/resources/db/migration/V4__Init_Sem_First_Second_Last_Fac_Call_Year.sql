create table semestr(
    semestr_id bigint not null,
    name varchar(30) not null,
    primary key (semestr_id)
);

create table firstname
(
    firstname_id bigint not null,
    name varchar(100) not null,
    primary key (firstname_id)
);

create table secondname
(
    secondname_id bigint not null,
    name varchar(100) not null,
    primary key (secondname_id)
);

create table lastname
(
    lastname_id bigint not null,
    name varchar(100) not null,
    primary key (lastname_id)
);

create table facultat(
    facultat_id bigint not null,
    name varchar(100) not null,
    forma varchar(30) not null,
    primary key (facultat_id)
);

create table call
(
    call_id bigint not null,
    name varchar(100) not null,
    primary key (call_id)
);

create table year
(
    year_id bigint not null,
    firstnameyear varchar(30) not null,
    secondnameyear varchar(30) not null,
    primary key (year_id)
);


    insert into call (call_id, name)
    values
           (1, '08:00-09:30'),
           (2, '09:40-11:10'),
           (3, '11:20-12:50'),
           (4, '13:20-14:50'),
           (5, '15:00-16:30'),
           (6, '16:40-18:10'),
           (7, '18:20-19:50'),
           (8, '20:55-22:25'),
           (9, '22:29-23:59'),
           (10, ' ');