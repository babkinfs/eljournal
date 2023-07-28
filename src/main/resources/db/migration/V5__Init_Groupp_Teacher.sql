
create table teacher(
      teacher_id bigint not null,
      role  varchar(10),
      firstname_id bigint,
      lastname_id bigint,
      secondname_id bigint,
      user_id bigint,
      primary key (teacher_id)
);

alter table if exists teacher
    add constraint teacher_firstname_fk
    foreign key (firstname_id) references firstname;

alter table if exists teacher
    add constraint teacher_lastname_fk
    foreign key (lastname_id) references lastname;

alter table if exists teacher
    add constraint teacher_secondname_fk
    foreign key (secondname_id) references secondname;

alter table if exists teacher
    add constraint teacher_user_fk
    foreign key (user_id) references usr;

create table subgroupp
(
    subgroupp_id bigint not null,
    namesubgroupp  varchar(10),
    primary key (subgroupp_id)
);


create table groupp
(
    groupp_id bigint not null,
    namegroupp varchar(100),
    subgroupp_id int,
    semestr_id bigint,
    year_id bigint,
    facultat_id bigint,
    primary key (groupp_id)
);

create table startdata
(
    startdata_id bigint not null,
    role varchar(100),
    email varchar(100),
    firstname_id bigint,
    lastname_id bigint,
    secondname_id bigint,
    groupp_id bigint,
    primary key (startdata_id)
);

create table student
(
    student_id bigint not null,
    firstname_id bigint,
    secondname_id bigint,
    lastname_id bigint,
    user_id bigint,
    groupp_id bigint,
    primary key (student_id)
);

create table standart
(
    standart_id bigint not null,
    namecourse varchar(255),
    primary key (standart_id)
);

create table course
(
    course_id bigint not null,
    name_course_full varchar(255),
    name_course_short varchar(255),
    kolzan int,
    groupp_id bigint,
    teacher_id bigint,
    primary key (course_id)
);


alter table if exists student
    add constraint student_firstname_fk
    foreign key (firstname_id) references firstname;

alter table if exists student
    add constraint student_lastname_fk
    foreign key (lastname_id) references lastname;

alter table if exists student
    add constraint student_secondname_fk
    foreign key (secondname_id) references secondname;

alter table if exists student
    add constraint student_groupp_fk
    foreign key (groupp_id) references groupp;



alter table if exists startdata
    add constraint startdata_firstname_fk
    foreign key (firstname_id) references firstname;

alter table if exists startdata
    add constraint startdata_lastname_fk
    foreign key (lastname_id) references lastname;

alter table if exists startdata
    add constraint startdata_secondname_fk
    foreign key (secondname_id) references secondname;

alter table if exists startdata
    add constraint startdata_groupp_fk
    foreign key (groupp_id) references groupp;




alter table if exists groupp
    add constraint groupp_semestr_fk
    foreign key (semestr_id) references semestr;

alter table if exists groupp
    add constraint groupp_year_fk
    foreign key (year_id) references year;

alter table if exists groupp
    add constraint groupp_facultat_fk
    foreign key (facultat_id) references facultat;

alter table if exists groupp
    add constraint groupp_subgroupp_fk
    foreign key (subgroupp_id) references subgroupp;


alter table if exists course
    add constraint course_groupp_fk
    foreign key (groupp_id) references groupp;

alter table if exists course
    add constraint course_teacher_fk
    foreign key (teacher_id) references teacher;


