create table film (
    file_id bigint not null auto_increment,
    director_id bigint not null,
    description varchar(255) not null,
    genre varchar(255) not null,
    poster_img varchar(255) not null,
    release_ate date not null,
    running_time integer not null,
    subtitle varchar(255),
    title varchar(255) not null,
    primary key (file_id)
) engine=InnoDB;