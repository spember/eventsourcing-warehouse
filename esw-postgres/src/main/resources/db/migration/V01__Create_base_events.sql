create table events (
    entity_id varchar(32) not null,
    revision integer not null,
    instant_observed timestamp not null,
    instant_occurred timestamp not null,
    user_id varchar(100) not null,
    type varchar(100) not null,
    data jsonb not null,
    UNIQUE (entity_id, revision)
);

create index if not exists event_observed on events(instant_observed);
create index if not exists event_occurred on events(instant_occurred);
create index if not exists event_type on events(type);

