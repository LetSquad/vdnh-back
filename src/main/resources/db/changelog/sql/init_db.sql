CREATE TABLE location_type(
    code TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    name_en TEXT NOT NULL,
    name_cn TEXT
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

CREATE TABLE schedule(
    id BIGINT PRIMARY KEY,
    monday TEXT,
    tuesday TEXT,
    wednesday TEXT,
    thursday TEXT,
    friday TEXT,
    saturday TEXT,
    sunday TEXT,
    additional_info TEXT
);

CREATE TABLE place(
    id BIGINT PRIMARY KEY,
    title TEXT NOT NULL,
    title_en TEXT,
    title_cn TEXT,
    priority INTEGER,
    url TEXT NOT NULL,
    image_url TEXT,
    tickets_url TEXT,
    is_active BOOLEAN,
    coordinates_id BIGINT NOT NULL REFERENCES coordinates(id),
    schedule_id BIGINT REFERENCES schedule(id),
    type_code TEXT NOT NULL REFERENCES location_type(code),
    subject_code TEXT REFERENCES location_subject(code),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE event(
    id BIGINT PRIMARY KEY,
    title TEXT NOT NULL,
    title_en TEXT,
    title_cn TEXT,
    priority INTEGER,
    url TEXT NOT NULL,
    image_url TEXT,
    is_active BOOLEAN,
    start_date DATE,
    finish_date DATE,
    coordinates_id BIGINT REFERENCES coordinates(id),
    schedule_id BIGINT REFERENCES schedule(id),
    type_code TEXT NOT NULL REFERENCES location_type(code),
    subject_code TEXT REFERENCES location_subject(code),
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE event_place(
    event_id BIGINT REFERENCES event(id),
    place_id BIGINT REFERENCES place(id),
    PRIMARY KEY (event_id, place_id)
);
