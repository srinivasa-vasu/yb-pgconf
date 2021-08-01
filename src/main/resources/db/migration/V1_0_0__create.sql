-- DROP TABLE IF EXISTS todo;
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- fault tolerance

  CREATE TABLE IF NOT EXISTS todo
(
    id           uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    task         VARCHAR(255),
    geo_code     VARCHAR(4),
    status       boolean
);

-- geo partition tables

/*CREATE TABLE todo
(
    id           uuid NOT NULL DEFAULT uuid_generate_v4(),
    task         VARCHAR(255),
    geo_code     VARCHAR(4),
    status       boolean
) PARTITION BY LIST (geo_code);

CREATE
TABLESPACE us_central_1 WITH (
  replica_placement='{"num_replicas": 1, "placement_blocks":
  [{"cloud":"gcp","region":"us-central-1","zone":"us-central-1a","min_num_replicas":1}]}'
);

CREATE
TABLESPACE ap_south_1 WITH (
  replica_placement='{"num_replicas": 1, "placement_blocks":
  [{"cloud":"gcp","region":"ap-south-1","zone":"ap-south-1a","min_num_replicas":1}]}'
);

CREATE
TABLESPACE eu_west_1 WITH (
  replica_placement='{"num_replicas": 1, "placement_blocks":
  [{"cloud":"gcp","region":"eu-west-1","zone":"eu-west-1a","min_num_replicas":1}]}'
);


CREATE TABLE todo_eu
    PARTITION OF todo
(id, task, geo_code, status,
    PRIMARY KEY(id HASH, geo_code))
    FOR VALUES IN ('EU') TABLESPACE eu_west_1;

CREATE TABLE todo_ap
    PARTITION OF todo
(id, task, geo_code, status,
 PRIMARY KEY(id HASH, geo_code))
    FOR VALUES IN ('AS') TABLESPACE ap_south_1;

CREATE TABLE todo_us
    PARTITION OF todo
(id, task, geo_code, status,
 PRIMARY KEY(id HASH, geo_code))
    FOR VALUES IN ('NA') TABLESPACE us_central_1;*/
