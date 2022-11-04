CREATE TABLE location_type(
    code TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    name_en TEXT NOT NULL,
    name_cn TEXT,
    icon_code TEXT NOT NULL,
    icon_color TEXT NOT NULL
);

CREATE TABLE location_subject(
    code TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    name_en TEXT NOT NULL,
    name_cn TEXT
);

CREATE TABLE coordinates(
    id BIGINT PRIMARY KEY,
    latitude NUMERIC(18, 12) NOT NULL,
    longitude NUMERIC(18, 12) NOT NULL,
    connections JSONB NOT NULL,
    load_factor JSONB
);

CREATE TABLE place(
    id BIGINT PRIMARY KEY,
    title TEXT NOT NULL,
    title_en TEXT,
    title_cn TEXT,
    priority INTEGER NOT NULL,
    visit_time_minutes INTEGER NOT NULL,
    placement TEXT NOT NULL,
    payment_conditions TEXT NOT NULL,
    url TEXT NOT NULL,
    image_url TEXT,
    tickets_url TEXT,
    is_active BOOLEAN,
    schedule JSONB,
    coordinates_id BIGINT NOT NULL REFERENCES coordinates(id),
    type_code TEXT NOT NULL REFERENCES location_type(code),
    subject_code TEXT REFERENCES location_subject(code),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE event(
    id BIGINT PRIMARY KEY,
    title TEXT NOT NULL,
    title_en TEXT,
    title_cn TEXT,
    priority INTEGER NOT NULL,
    visit_time_minutes INTEGER NOT NULL,
    placement TEXT NOT NULL,
    payment_conditions TEXT NOT NULL,
    url TEXT NOT NULL,
    image_url TEXT,
    is_active BOOLEAN,
    start_date DATE,
    finish_date DATE,
    schedule JSONB,
    coordinates_id BIGINT REFERENCES coordinates(id),
    type_code TEXT NOT NULL REFERENCES location_type(code),
    subject_code TEXT REFERENCES location_subject(code),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE event_place(
    event_id BIGINT REFERENCES event(id),
    place_id BIGINT REFERENCES place(id),
    PRIMARY KEY (event_id, place_id)
);

CREATE TABLE route(
    id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT NOT NULL,
    distance_meters INTEGER,
    duration_minutes INTEGER
);

CREATE TABLE route_place(
    route_id BIGINT REFERENCES route (id),
    place_id BIGINT REFERENCES place (id),
    place_order INTEGER,
    PRIMARY KEY (route_id, place_id)
);
