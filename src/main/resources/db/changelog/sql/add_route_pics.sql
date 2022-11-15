alter table route
    drop column distance_meters;

alter table route
    drop column duration_minutes;

alter table route
    add image_url text;

alter table route
    add preview_image_url text;