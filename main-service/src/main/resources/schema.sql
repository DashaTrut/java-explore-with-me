drop table IF EXISTS users CASCADE;
drop table IF EXISTS categories CASCADE;
drop table IF EXISTS events CASCADE;
drop table IF EXISTS participation CASCADE;
drop table IF EXISTS compilations CASCADE;
drop table IF EXISTS event_compilations CASCADE;

create table users (
  id integer GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL UNIQUE,
  CONSTRAINT pk_user PRIMARY KEY (id)
  );

create table  categories (
  id integer GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL UNIQUE,
  CONSTRAINT pk_category PRIMARY KEY (id)
);

create TABLE events (
  id integer GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  category_id integer NOT NULL REFERENCES categories(id),
  confirmed_requests integer NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  initiator_id integer NOT NULL REFERENCES users(id) ON delete CASCADE,
  lat REAL NOT NULL,
  lon REAL NOT NULL,
  paid BOOLEAN ,
  participant_limit INTEGER NOT NULL,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  request_moderation BOOLEAN NOT NULL,
  state VARCHAR(20) NOT NULL,
  title VARCHAR(120) NOT NULL,
  CONSTRAINT pk_event PRIMARY KEY (id)
);

create TABLE participation (
  id integer GENERATED BY DEFAULT AS IDENTITY,
  created TIMESTAMP WITHOUT TIME ZONE ,
  event_id integer NOT NULL REFERENCES events(id) ON delete CASCADE,
  requester_id integer NOT NULL REFERENCES users(id) ON delete CASCADE,
  status VARCHAR(20) NOT NULL,
  CONSTRAINT pk_request PRIMARY KEY (id),
  CONSTRAINT uq_ev_req_id UNIQUE(event_id, requester_id)
);

create TABLE compilations (
  id integer GENERATED BY DEFAULT AS IDENTITY,
  pinned BOOLEAN NOT NULL,
  title VARCHAR(50) NOT NULL,
  CONSTRAINT pk_compilation PRIMARY KEY (id)
);

create TABLE event_compilations (
  event_id integer NOT NULL REFERENCES events(id) ON delete CASCADE,
  compilation_id integer NOT NULL REFERENCES compilations(id) ON delete CASCADE,
  CONSTRAINT pk_ec PRIMARY KEY (event_id, compilation_id)
);