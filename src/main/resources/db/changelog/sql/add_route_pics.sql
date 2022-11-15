alter table route
    drop column distance_meters;

alter table route
    add image_url text;

alter table route
    add preview_image_url text;

alter table route
    add name_en text;

alter table route
    add name_cn text;

alter table route
    add description_en text;

alter table route
    add description_cn text;

alter table route_place
    add description text;

alter table route_place
    add description_en text;

alter table route_place
    add description_cn text;

alter table route
    rename column duration_minutes to duration_seconds;