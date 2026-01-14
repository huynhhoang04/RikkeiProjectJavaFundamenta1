create table admin(
    id serial,
    username varchar(50) not null unique ,
    password varchar(255) not null ,

    primary key (id)
);


create table student(
    id serial,
    name varchar(100) not null ,
    dob date not null ,
    email varchar(100) not null unique ,
    gender bit not null ,
    phone varchar(20) not null ,
    password varchar(255) not null ,
    created_at date default current_timestamp,

    primary key (id)
);


create table course(
    id serial,
    name varchar(100) not null ,
    duration int not null ,
    instructor varchar(100) not null ,
    created_at date default current_timestamp,

    primary key (id)
);

create type course_status as enum('WAITING','DENIED','CANCER','CONFIRM');

create table enrollment(
    id serial,
    student_id int ,
    course_id int ,
    registered_at date default current_timestamp,
    status course_status default 'WAITING',

    primary key (id),
    foreign key (student_id) references student(id),
    foreign key (course_id) references course(id)
);
