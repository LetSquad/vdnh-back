CREATE TABLE route
(
    id          BIGINT PRIMARY KEY,
    name        TEXT NOT NULL,
    description TEXT NOT NULL,
    distance    int,
    duration    int
);

CREATE TABLE route_place
(
    route_id    BIGINT REFERENCES route (id),
    place_id    BIGINT REFERENCES place (id),
    place_order int,
    PRIMARY KEY (route_id, place_id)
);