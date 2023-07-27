create table dnevnik(
       dnevnik_id bigint not null,
       ispresent boolean not null,
       ochno boolean not null,
       student_id bigint,
       raspisanie_id bigint,
       primary key (dnevnik_id)
);

alter table if exists dnevnik
    add constraint dnevnik_student_fk
    foreign key (student_id) references student;

alter table if exists dnevnik
    add constraint dnevnik_raspisanie_fk
    foreign key (raspisanie_id) references raspisanie;

create table filesfordnevnik(
                                filesfordnevnik_id bigint not null,
                                datesdachi varchar(100) not null,
                                ktocdal varchar(100) not null,
                                pathstudent varchar(200) not null,
                                datecontrol varchar(100),
                                pathteacher varchar(200),
                                status varchar(100),
                                ocenka varchar(100),
                                dnevnik_id bigint,
                        primary key (filesfordnevnik_id)
);

alter table if exists filesfordnevnik
    add constraint filesfordnevnik_dnevnik_fk
    foreign key (dnevnik_id) references dnevnik;
