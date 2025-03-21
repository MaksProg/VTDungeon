DROP TABLE IF EXIST human CASCADE;
DROP TABLE IF EXIST lake CASCADE;
DROP TABLE IF EXIST road CASCADE;
DROP TABLE IF EXIST traces CASCADE;
DROP TABLE IF EXIST openspace CASCADE;
DROP TABLE IF EXIST forest CASCADE;
DROP TABLE IF EXIST forest_road CASCADE;

CREATE TABLE human(
	human_id SERIAL PRIMARY KEY,
	name VARCHAR(64) NOT NULL,
	age INTEGER NOT NULL CHECK(age>0),
	road_id INTEGER REFERENCES road(road_id)
);

CREATE TABLE road(
	road_id SERIAL PRIMARY KEY,
	shape VARCHAR(64) NOT NULL,
	length INTEGER NOT NULL CHECK(length>0),
	forest_id INTEGER REFERENCES forest(forest_id),
	openspace INTEGER REFERENCES openspace(openspace_id)
);

CREATE TABLE traces(
	traces_id SERIAL PRIMARY KEY,
	description VARCHAR(64) NOT NULL,
	human_id INTEGER REFERENCES human(human_id)
);

CREATE TABLE openspace(
	openspace_id SERIAL PRIMARY KEY,
	width INTEGER NOT NULL CHECK(width>0),
	length INTEGER NOT NULL CHECK(length>0),
	forest_id INTEGER REFERENCES forest(forest_id),
	traces_id INTEGER REFERENCES traces(traces_id)
);

CREATE TABLE lake(
	lake_id SERIAL,
	depth INTEGER NOT NULL CHECK(depth>0),
	have_fishes BOOLEAN
);

CREATE TABLE forest(
	forest_id SERIAL PRIMARY KEY,
	length INTEGER NOT NULL CHECK(length>0),
	density DECIMAL(3,2) NOT NULL CHECK(density > 0 and density<=1)
);
CREATE TABLE forest_road(
	road_id INTEGER REFERENCES road(road_id),
	forest_id INTEGER REFERENCES forest(forest_id),
	PRIMARY KEY(road_id,forest_id)
);