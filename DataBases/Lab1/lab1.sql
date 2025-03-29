DROP TABLE IF EXISTS lake CASCADE;
DROP TABLE IF EXISTS openspace CASCADE;
DROP TABLE IF EXISTS traces CASCADE;
DROP TABLE IF EXISTS forest_road CASCADE;
DROP TABLE IF EXISTS road CASCADE;
DROP TABLE IF EXISTS human CASCADE;
DROP TABLE IF EXISTS forest CASCADE;

CREATE TABLE forest(
	forest_id SERIAL PRIMARY KEY,
	length INTEGER NOT NULL CHECK(length>0),
	density DECIMAL(3,2) NOT NULL CHECK(density > 0 and density<=1)
);

CREATE TABLE human(
	human_id SERIAL PRIMARY KEY,
	name VARCHAR(64) NOT NULL,
	age INTEGER NOT NULL CHECK(age>0)
);

CREATE TABLE road(
	road_id SERIAL PRIMARY KEY,
	shape VARCHAR(64) NOT NULL,
	length INTEGER NOT NULL CHECK(length>0),
	forest_id INTEGER REFERENCES forest(forest_id)
);

CREATE TABLE forest_road(
	road_id INTEGER REFERENCES road(road_id),
	forest_id INTEGER REFERENCES forest(forest_id),
	PRIMARY KEY(road_id,forest_id)
);

CREATE TABLE traces(
	traces_id SERIAL PRIMARY KEY,
	description VARCHAR(64) NOT NULL,
	human_id INTEGER REFERENCES human(human_id) ON DELETE SET NULL
);

CREATE TABLE openspace(
	openspace_id SERIAL PRIMARY KEY,
	width INTEGER NOT NULL CHECK(width>0),
	length INTEGER NOT NULL CHECK(length>0),
	forest_id INTEGER REFERENCES forest(forest_id),
	traces_id INTEGER REFERENCES traces(traces_id)
);

CREATE TABLE lake(
	lake_id SERIAL PRIMARY KEY,
	depth INTEGER NOT NULL CHECK(depth>0),
	have_fishes BOOLEAN,
	human_id INTEGER REFERENCES human(human_id)
);


INSERT INTO forest (length, density) VALUES (3000,0.52);
INSERT INTO forest (length, density) VALUES (1000,0.95);

INSERT INTO human (name, age) VALUES ('Misha', 18);
INSERT INTO human (name, age) VALUES ('Ivan', 20);

INSERT INTO road (shape, length,forest_id) VALUES ('straight', 200,1);
INSERT INTO road (shape, length,forest_id) VALUES ('curved', 150,2);

INSERT INTO forest_road (road_id,forest_id) VALUES (1,1);
INSERT INTO forest_road (road_id,forest_id) VALUES (2,2);

INSERT INTO traces (description, human_id) VALUES ('barefoot', 1);
INSERT INTO traces (description, human_id) VALUES ('boots', 2);

INSERT INTO openspace (width,length,forest_id,traces_id) VALUES (50,250,1,1);
INSERT INTO openspace (width,length,forest_id,traces_id) VALUES (35,278,2,2);

INSERT INTO lake (human_id, depth, have_fishes) VALUES (1,300, TRUE);
