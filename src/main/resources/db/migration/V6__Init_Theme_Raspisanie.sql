create table theme(
       theme_id bigint not null,
       nameteme varchar(1024) not null,
       zadanie varchar(1024) not null,
       number int,
       typezan varchar(20) not null,
       fileshablon varchar(256) not null,
       fileforstudent varchar(256) not null,
       course_id bigint,
       primary key (theme_id)
);

create table raspisanie(
    raspisanie_id bigint not null,
    actiondate varchar(100) not null,
    number int,
    template varchar(256),
    call_id bigint,
    theme_id bigint,
    course_id bigint,
    primary key (raspisanie_id)
);

create table exercise(
    exercise_id bigint not null,
    key varchar(100) not null,
    body varchar(100) not null,
    startdata_id bigint,
    theme_id bigint,
    primary key (exercise_id)
);

create table shabloncourse(
    shabloncourse_id bigint not null,
    name varchar(2048) not null,
    typez varchar(10) not null,
    course_id bigint,
    primary key (shabloncourse_id)
);


alter table if exists raspisanie
    add constraint raspisanie_call_fk
    foreign key (call_id) references call;

alter table if exists raspisanie
    add constraint raspisanie_course_fk
    foreign key (course_id) references course;

alter table if exists theme
    add constraint theme_course_fk
    foreign key (course_id) references course;

alter table if exists raspisanie
    add constraint raspisanie_theme_fk
    foreign key (theme_id) references theme;

alter table if exists exercise
    add constraint exercise_startdata_fk
    foreign key (startdata_id) references startdata;

alter table if exists exercise
    add constraint exercise_theme_fk
    foreign key (theme_id) references theme;

alter table if exists shabloncourse
    add constraint shabloncourse_course_fk
    foreign key (course_id) references course;
