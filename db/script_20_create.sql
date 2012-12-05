--
-- Copyright (C) 2012
-- by 52 North Initiative for Geospatial Open Source Software GmbH
--
-- Contact: Andreas Wytzisk
-- 52 North Initiative for Geospatial Open Source Software GmbH
-- Martin-Luther-King-Weg 24
-- 48155 Muenster, Germany
-- info@52north.org
--
-- This program is free software; you can redistribute and/or modify it under
-- the terms of the GNU General Public License version 2 as published by the
-- Free Software Foundation.
--
-- This program is distributed WITHOUT ANY WARRANTY; even without the implied
-- WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
-- General Public License for more details.
--
-- You should have received a copy of the GNU General Public License along with
-- this program (see gnu-gpl v2.txt). If not, write to the Free Software
-- Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
-- visit the Free Software Foundation web page, http://www.fsf.org.
--

DROP TABLE IF EXISTS global_settings CASCADE;
DROP TABLE IF EXISTS result_type CASCADE;
DROP TABLE IF EXISTS spatial_filtering_profile CASCADE;
DROP TABLE IF EXISTS sensor_system CASCADE;
DROP TABLE IF EXISTS offering CASCADE;
DROP TABLE IF EXISTS procedure_description_format CASCADE;
DROP TABLE IF EXISTS related_feature CASCADE;
DROP TABLE IF EXISTS observation_type CASCADE;
DROP TABLE IF EXISTS swe_type CASCADE;
DROP TABLE IF EXISTS composite_phenomenon CASCADE;
DROP TABLE IF EXISTS feature_of_interest_type CASCADE;
DROP TABLE IF EXISTS geometry_value CASCADE;
DROP TABLE IF EXISTS text_value CASCADE;
DROP TABLE IF EXISTS category_value CASCADE;
DROP TABLE IF EXISTS unit CASCADE;
DROP TABLE IF EXISTS numeric_value CASCADE;
DROP TABLE IF EXISTS count_value CASCADE;
DROP TABLE IF EXISTS boolean_value CASCADE;
DROP TABLE IF EXISTS request CASCADE;
DROP TABLE IF EXISTS procedure CASCADE;
DROP TABLE IF EXISTS feature_relation CASCADE;
DROP TABLE IF EXISTS feature_of_interest CASCADE;
DROP TABLE IF EXISTS observable_property CASCADE;
DROP TABLE IF EXISTS procedure_has_observation_type CASCADE;
DROP TABLE IF EXISTS offering_has_allowed_observation_type CASCADE;
DROP TABLE IF EXISTS quality CASCADE;
DROP TABLE IF EXISTS offering_has_related_feature CASCADE;
DROP TABLE IF EXISTS procedure_has_feature_of_interest_type CASCADE;
DROP TABLE IF EXISTS observation_template CASCADE;
DROP TABLE IF EXISTS request_has_composite_phenomenon CASCADE;
DROP TABLE IF EXISTS request_has_observable_property CASCADE;
DROP TABLE IF EXISTS observation_constellation CASCADE;
DROP TABLE IF EXISTS valid_procedure_time CASCADE;
DROP TABLE IF EXISTS composite_phenomenon_has_observable_property CASCADE;
DROP TABLE IF EXISTS observation CASCADE;
DROP TABLE IF EXISTS observation_has_text_value CASCADE;
DROP TABLE IF EXISTS observation_has_category_value CASCADE;
DROP TABLE IF EXISTS result_template CASCADE;
DROP TABLE IF EXISTS observation_has_numeric_value CASCADE;
DROP TABLE IF EXISTS observation_has_count_value CASCADE;
DROP TABLE IF EXISTS observation_has_boolean_value CASCADE;
DROP TABLE IF EXISTS observation_has_quality CASCADE;
DROP TABLE IF EXISTS observation_has_spatial_filtering_profile CASCADE;
DROP TABLE IF EXISTS observation_has_geometry_value CASCADE;
DROP TABLE IF EXISTS related_feature_has_related_feature_role CASCADE;
DROP TABLE IF EXISTS related_feature_role CASCADE;

-- drop sequences
DROP SEQUENCE IF EXISTS boolean_value_id_seq;
DROP SEQUENCE IF EXISTS category_value_id_seq;
DROP SEQUENCE IF EXISTS composite_phenomenon_id_seq;
DROP SEQUENCE IF EXISTS count_value_id_seq;
DROP SEQUENCE IF EXISTS feature_of_interest_id_seq;
DROP SEQUENCE IF EXISTS feature_of_interest_type_id_seq;
DROP SEQUENCE IF EXISTS geometry_value_id_seq;
DROP SEQUENCE IF EXISTS numeric_value_id_seq;
DROP SEQUENCE IF EXISTS observable_property_id_seq;
DROP SEQUENCE IF EXISTS observation_constellation_id_seq;
DROP SEQUENCE IF EXISTS observation_id_seq;
DROP SEQUENCE IF EXISTS observation_template_id_seq;
DROP SEQUENCE IF EXISTS observation_type_id_seq;
DROP SEQUENCE IF EXISTS offering_id_seq;
DROP SEQUENCE IF EXISTS procedure_description_format_id_seq;
DROP SEQUENCE IF EXISTS procedure_id_seq;
DROP SEQUENCE IF EXISTS quality_id_seq;
DROP SEQUENCE IF EXISTS related_feature_id_seq;
DROP SEQUENCE IF EXISTS related_feature_role_id_seq;
DROP SEQUENCE IF EXISTS request_id_seq;
DROP SEQUENCE IF EXISTS result_template_id_seq;
DROP SEQUENCE IF EXISTS result_type_id_seq;
DROP SEQUENCE IF EXISTS spatial_filtering_profile_id_seq;
DROP SEQUENCE IF EXISTS swe_type_id_seq;
DROP SEQUENCE IF EXISTS text_value_id_seq;
DROP SEQUENCE IF EXISTS unit_id_seq;
DROP SEQUENCE IF EXISTS valid_procedure_time_id_seq;

-- create sequences
CREATE SEQUENCE boolean_value_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE category_value_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE composite_phenomenon_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE count_value_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE feature_of_interest_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE feature_of_interest_type_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE geometry_value_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE numeric_value_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE observable_property_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE observation_constellation_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE observation_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE observation_template_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE observation_type_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE offering_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE procedure_description_format_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE procedure_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE quality_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE related_feature_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE related_feature_role_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE request_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE result_template_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE result_type_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE spatial_filtering_profile_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE swe_type_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE text_value_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE unit_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;
CREATE SEQUENCE valid_procedure_time_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;

CREATE TABLE global_settings (
  key VARCHAR(256) NOT NULL,
  value TEXT,
  PRIMARY KEY(key)
);

CREATE TABLE result_type (
  result_type_id bigint NOT NULL DEFAULT nextval('result_type_id_seq'),
  result_type TEXT NOT NULL,
  UNIQUE (result_type),
  PRIMARY KEY(result_type_id)
);

CREATE TABLE related_feature_role (
  related_feature_role_id bigint NOT NULL DEFAULT nextval('related_feature_role_id_seq'),
  related_feature_role TEXT NOT NULL,
  UNIQUE (related_feature_role),
  PRIMARY KEY(related_feature_role_id)
);

CREATE TABLE spatial_filtering_profile (
  spatial_filtering_profile_id bigint NOT NULL DEFAULT nextval('spatial_filtering_profile_id_seq'),
  identifier TEXT NOT NULL,
  geom GEOMETRY NOT NULL,
  UNIQUE (identifier),
  PRIMARY KEY(spatial_filtering_profile_id)
);

CREATE TABLE offering (
  offering_id bigint NOT NULL DEFAULT nextval('offering_id_seq'),
  identifier TEXT NOT NULL,
  name TEXT NULL,
  UNIQUE (identifier),
  PRIMARY KEY(offering_id)
);

CREATE TABLE procedure_description_format (
  procedure_description_format_id bigint NOT NULL DEFAULT nextval('procedure_description_format_id_seq'),
  procedure_description_format TEXT NOT NULL,
  PRIMARY KEY(procedure_description_format_id)
);

CREATE TABLE related_feature (
  related_feature_id bigint NOT NULL DEFAULT nextval('related_feature_id_seq'),
  feature_of_interest_id INTEGER NULL,
  UNIQUE (feature_of_interest_id),
  PRIMARY KEY(related_feature_id)
);

CREATE TABLE observation_type (
  observation_type_id bigint NOT NULL DEFAULT nextval('observation_type_id_seq'),
  observation_type TEXT NOT NULL,
  UNIQUE (observation_type),
  PRIMARY KEY(observation_type_id)
);

CREATE TABLE swe_type (
  swe_type_id bigint NOT NULL DEFAULT nextval('swe_type_id_seq'),
  swe_type TEXT NOT NULL,
  UNIQUE (swe_type),
  PRIMARY KEY(swe_type_id)
);

CREATE TABLE composite_phenomenon (
  composite_phenomenon_id bigint NOT NULL DEFAULT nextval('composite_phenomenon_id_seq'),
  identifier TEXT NOT NULL,
  description TEXT NULL,
  UNIQUE (identifier),
  PRIMARY KEY(composite_phenomenon_id)
);

CREATE TABLE feature_of_interest_type (
  feature_of_interest_type_id bigint NOT NULL DEFAULT nextval('feature_of_interest_type_id_seq'),
  feature_of_interest_type TEXT NOT NULL,
  UNIQUE (feature_of_interest_type),
  PRIMARY KEY(feature_of_interest_type_id)
);

CREATE TABLE geometry_value (
  geometry_value_id bigint NOT NULL DEFAULT nextval('geometry_value_id_seq'),
  value GEOMETRY NOT NULL,
  UNIQUE (value),
  PRIMARY KEY(geometry_value_id)
);

CREATE TABLE text_value (
  text_value_id bigint NOT NULL DEFAULT nextval('text_value_id_seq'),
  value TEXT NOT NULL,
  UNIQUE (value),
  PRIMARY KEY(text_value_id)
);

CREATE TABLE category_value (
  category_value_id bigint NOT NULL DEFAULT nextval('category_value_id_seq'),
  value TEXT NOT NULL,
  UNIQUE (value),
  PRIMARY KEY(category_value_id)
);

CREATE TABLE unit (
  unit_id bigint NOT NULL DEFAULT nextval('unit_id_seq'),
  unit TEXT NOT NULL,
  UNIQUE (unit),
  PRIMARY KEY(unit_id)
);

CREATE TABLE numeric_value (
  numeric_value_id bigint NOT NULL DEFAULT nextval('numeric_value_id_seq'),
  value numeric NOT NULL,
  UNIQUE (value),
  PRIMARY KEY(numeric_value_id)
);

CREATE TABLE count_value (
  count_value_id bigint NOT NULL DEFAULT nextval('count_value_id_seq'),
  value integer NOT NULL,
  UNIQUE (value),
  PRIMARY KEY(count_value_id)
);

CREATE TABLE boolean_value (
  boolean_value_id bigint NOT NULL DEFAULT nextval('boolean_value_id_seq'),
  value boolean NOT NULL,
  UNIQUE (value),
  PRIMARY KEY(boolean_value_id)
);

CREATE TABLE request (
  request_id bigint NOT NULL DEFAULT nextval('request_id_seq'),
  offering_id INTEGER NOT NULL,
  request TEXT NOT NULL,
  begin_lease TIMESTAMP NULL,
  end_lease TIMESTAMP NOT NULL,
  PRIMARY KEY(request_id)
);

CREATE TABLE procedure (
  procedure_id bigint NOT NULL DEFAULT nextval('procedure_id_seq'),
  procedure_description_format_id INTEGER NOT NULL,
  identifier TEXT NOT NULL,
  deleted BOOL NOT NULL,
  UNIQUE (identifier),
  PRIMARY KEY(procedure_id)
);

CREATE TABLE sensor_system (
  parent_sensor_id INTEGER NOT NULL,
  child_sensor_id INTEGER NOT NULL,
  PRIMARY KEY(parent_sensor_id, child_sensor_id)
);

CREATE TABLE feature_of_interest (
  feature_of_interest_id bigint NOT NULL DEFAULT nextval('feature_of_interest_id_seq'),
  feature_of_interest_type_id INTEGER NOT NULL,
  identifier TEXT NULL,
  name Text NULL,
  geom GEOMETRY NULL,
  description_xml TEXT NULL,
  url TEXT NULL,
  UNIQUE (geom),
  UNIQUE (identifier,geom),
  UNIQUE (url),
  PRIMARY KEY(feature_of_interest_id)
);

CREATE TABLE feature_relation (
  parent_feature_id INTEGER NOT NULL,
  child_feature_id INTEGER NOT NULL,
  PRIMARY KEY(parent_feature_id, child_feature_id)
);

CREATE TABLE observable_property (
  observable_property_id bigint NOT NULL DEFAULT nextval('observable_property_id_seq'),
  identifier TEXT NOT NULL,
  description TEXT NULL,
  UNIQUE (identifier),
  PRIMARY KEY(observable_property_id)
);

CREATE TABLE procedure_has_observation_type (
  procedure_id INTEGER NOT NULL,
  observation_type_id INTEGER NOT NULL,
  PRIMARY KEY(procedure_id, observation_type_id)
);

CREATE TABLE offering_has_allowed_observation_type (
  offering_id INTEGER NOT NULL,
  observation_type_id INTEGER NOT NULL,
  PRIMARY KEY(offering_id, observation_type_id)
);

CREATE TABLE quality (
  quality_id bigint NOT NULL DEFAULT nextval('quality_id_seq'),
  unit_id INTEGER NOT NULL,
  swe_type_id INTEGER NOT NULL,
  name TEXT NOT NULL,
  value TEXT NOT NULL,
  PRIMARY KEY(quality_id)
);

CREATE TABLE offering_has_related_feature (
  offering_id INTEGER NOT NULL,
  related_feature_id INTEGER NOT NULL,
  PRIMARY KEY(offering_id, related_feature_id)
);

CREATE TABLE procedure_has_feature_of_interest_type (
  procedure_id INTEGER NOT NULL,
  feature_of_interest_type_id INTEGER NOT NULL,
  PRIMARY KEY(procedure_id, feature_of_interest_type_id)
);

CREATE TABLE observation_template (
  observation_template_id bigint NOT NULL DEFAULT nextval('observation_template_id_seq'),
  procedure_id INTEGER NOT NULL,
  request_id INTEGER NOT NULL,
  observation_template TEXT NULL,
  PRIMARY KEY(observation_template_id)
);

CREATE TABLE request_has_composite_phenomenon (
  request_id INTEGER NOT NULL,
  composite_phenomenon_id INTEGER NOT NULL,
  PRIMARY KEY(request_id, composite_phenomenon_id)
);

CREATE TABLE request_has_observable_property (
  request_id INTEGER NOT NULL,
  observable_property_id INTEGER NOT NULL,
  PRIMARY KEY(request_id, observable_property_id)
);

CREATE TABLE related_feature_has_related_feature_role (
  related_feature_id INTEGER NOT NULL,
  related_feature_role_id INTEGER NOT NULL,
  PRIMARY KEY(related_feature_id, related_feature_role_id)
);

CREATE TABLE observation_constellation (
  observation_constellation_id bigint NOT NULL DEFAULT nextval('observation_constellation_id_seq'),
  observation_type_id INTEGER NULL,
  procedure_id INTEGER NOT NULL,
  result_type_id INTEGER NULL,
  offering_id INTEGER NOT NULL,
  observable_property_id INTEGER NOT NULL,
  UNIQUE (observation_type_id,procedure_id,result_type_id,offering_id,observable_property_id),
  PRIMARY KEY(observation_constellation_id)
);

CREATE TABLE valid_procedure_time (
  valid_procedure_time_id bigint NOT NULL DEFAULT nextval('valid_procedure_time_id_seq'),
  procedure_id INTEGER NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NULL,
  description_url TEXT NULL,
  description_xml TEXT NULL,
  UNIQUE (procedure_id,start_time),
  PRIMARY KEY(valid_procedure_time_id)
);

CREATE TABLE composite_phenomenon_has_observable_property (
  composite_phenomenon_id INTEGER NOT NULL,
  observable_property_id INTEGER NOT NULL,
  PRIMARY KEY(composite_phenomenon_id, observable_property_id)
);

CREATE TABLE observation (
  observation_id bigint NOT NULL DEFAULT nextval('observation_id_seq'),
  feature_of_interest_id INTEGER NOT NULL,
  observation_constellation_id INTEGER NOT NULL,
  identifier TEXT NULL,
  phenomenon_time_start TIMESTAMP NOT NULL,
  phenomenon_time_end TIMESTAMP NULL,
  result_time TIMESTAMP NULL,
  valid_time_start TIMESTAMP NULL,
  valid_time_end TIMESTAMP NULL,
  unit_id INTEGER NULL,
  anti_subsetting TEXT NULL,
  deleted BOOL NOT NULL DEFAULT false,
  UNIQUE (feature_of_interest_id,observation_constellation_id,phenomenon_time_start,identifier),
  PRIMARY KEY(observation_id)
  );

CREATE TABLE observation_has_text_value (
  observation_id INTEGER NOT NULL,
  text_value_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, text_value_id)
);

CREATE TABLE observation_has_category_value (
  observation_id INTEGER NOT NULL,
  category_value_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, category_value_id)
);

CREATE TABLE result_template (
  result_template_id bigint NOT NULL DEFAULT nextval('result_template_id_seq'),
  observation_constellation_id INTEGER NOT NULL,
  feature_of_interest_id INTEGER NOT NULL,
  identifier TEXT NOT NULL,
  result_structure TEXT NOT NULL,
  result_encoding TEXT NOT NULL,
  UNIQUE (feature_of_interest_id,observation_constellation_id,identifier,result_structure,result_encoding),
  PRIMARY KEY(result_template_id)
);

CREATE TABLE observation_has_numeric_value (
  observation_id INTEGER NOT NULL,
  numeric_value_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, numeric_value_id)
);

CREATE TABLE observation_has_count_value (
  observation_id INTEGER NOT NULL,
  count_value_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, count_value_id)
);

CREATE TABLE observation_has_boolean_value (
  observation_id INTEGER NOT NULL,
  boolean_value_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, boolean_value_id)
);

CREATE TABLE observation_has_quality (
  observation_id INTEGER NOT NULL,
  quality_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, quality_id)
);

CREATE TABLE observation_has_spatial_filtering_profile (
  observation_id INTEGER NOT NULL,
  spatial_filtering_profile_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, spatial_filtering_profile_id)
);

CREATE TABLE observation_has_geometry_value (
  observation_id INTEGER NOT NULL,
  geometry_value_id INTEGER NOT NULL,
  PRIMARY KEY(observation_id, geometry_value_id)
);

--------------------------------------------------------------

CREATE INDEX request_FKIndex1 ON request(offering_id);
CREATE INDEX procedure_FKIndex1 ON procedure(procedure_description_format_id);
CREATE INDEX feature_of_interest_FKIndex1 ON feature_of_interest(feature_of_interest_type_id);
CREATE INDEX sensor_system_FKIndex1 ON sensor_system(parent_sensor_id);
CREATE INDEX sensor_system_FKIndex2 ON sensor_system(child_sensor_id);
CREATE INDEX observable_property_FKIndex1 ON observation(unit_id);
CREATE INDEX offering_has_allowed_observation_type_FKIndex1 ON offering_has_allowed_observation_type(offering_id);
CREATE INDEX offering_has_allowed_observation_type_FKIndex2 ON offering_has_allowed_observation_type(observation_type_id);
CREATE INDEX procedure_has_observation_type_FKIndex1 ON procedure_has_observation_type(procedure_id);
CREATE INDEX procedure_has_observation_type_FKIndex2 ON procedure_has_observation_type(observation_type_id);
CREATE INDEX quality_FKIndex1 ON quality(swe_type_id);
CREATE INDEX quality_FKIndex3 ON quality(unit_id);
CREATE INDEX offering_has_related_feature_FKIndex1 ON offering_has_related_feature(offering_id);
CREATE INDEX offering_has_related_feature_FKIndex2 ON offering_has_related_feature(related_feature_id);
CREATE INDEX feature_relation_FKIndex1 ON feature_relation(parent_feature_id);
CREATE INDEX feature_relation_FKIndex2 ON feature_relation(child_feature_id);
CREATE INDEX procedure_has_feature_of_interest_type_FKIndex1 ON procedure_has_feature_of_interest_type(procedure_id);
CREATE INDEX procedure_has_feature_of_interest_type_FKIndex2 ON procedure_has_feature_of_interest_type(feature_of_interest_type_id);
CREATE INDEX observation_template_FKIndex1 ON observation_template(request_id);
CREATE INDEX observation_template_FKIndex2 ON observation_template(procedure_id);
CREATE INDEX request_has_composite_phenomenon_FKIndex1 ON request_has_composite_phenomenon(request_id);
CREATE INDEX request_has_composite_phenomenon_FKIndex2 ON request_has_composite_phenomenon(composite_phenomenon_id);
CREATE INDEX request_has_observable_property_FKIndex1 ON request_has_observable_property(request_id);
CREATE INDEX request_has_observable_property_FKIndex2 ON request_has_observable_property(observable_property_id);
CREATE INDEX related_feature_has_related_feature_role_FKIndex1 ON related_feature_has_related_feature_role(related_feature_id);
CREATE INDEX related_feature_has_related_feature_role_FKIndex2 ON related_feature_has_related_feature_role(related_feature_role_id);
CREATE INDEX observation_constellation_FKIndex1 ON observation_constellation(offering_id);
CREATE INDEX observation_constellation_FKIndex ON observation_constellation(procedure_id);
CREATE INDEX observation_constellation_FKIndex3 ON observation_constellation(observable_property_id);
CREATE INDEX observation_constellation_FKIndex4 ON observation_constellation(result_type_id);
CREATE INDEX observation_constellation_FKIndex5 ON observation_constellation(observation_type_id);
CREATE INDEX valid_procedure_time_FKIndex1 ON valid_procedure_time(procedure_id);
CREATE INDEX composite_phenomenon_has_observable_property_FKIndex1 ON composite_phenomenon_has_observable_property(composite_phenomenon_id);
CREATE INDEX composite_phenomenon_has_observable_property_FKIndex2 ON composite_phenomenon_has_observable_property(observable_property_id);
CREATE INDEX observation_FKIndex1 ON observation(observation_constellation_id);
CREATE INDEX observation_FKIndex2 ON observation(feature_of_interest_id);
CREATE INDEX observation_has_text_value_FKIndex1 ON observation_has_text_value(observation_id);
CREATE INDEX observation_has_text_value_FKIndex2 ON observation_has_text_value(text_value_id);
CREATE INDEX observation_has_category_value_FKIndex1 ON observation_has_category_value(observation_id);
CREATE INDEX observation_has_category_value_FKIndex2 ON observation_has_category_value(category_value_id);
CREATE INDEX result_template_FKIndex1 ON result_template(feature_of_interest_id);
CREATE INDEX result_template_FKIndex3 ON result_template(observation_constellation_id);
CREATE INDEX observation_has_numeric_value_FKIndex1 ON observation_has_numeric_value(observation_id);
CREATE INDEX observation_has_numeric_value_FKIndex2 ON observation_has_numeric_value(numeric_value_id);
CREATE INDEX observation_has_count_value_FKIndex1 ON observation_has_count_value(observation_id);
CREATE INDEX observation_has_count_value_FKIndex2 ON observation_has_count_value(count_value_id);
CREATE INDEX observation_has_boolean_value_FKIndex1 ON observation_has_boolean_value(observation_id);
CREATE INDEX observation_has_boolean_value_FKIndex2 ON observation_has_boolean_value(boolean_value_id);
CREATE INDEX observation_has_quality_FKIndex1 ON observation_has_quality(observation_id);
CREATE INDEX observation_has_quality_FKIndex2 ON observation_has_quality(quality_id);
CREATE INDEX observation_has_spatial_filtering_profile_FKIndex1 ON observation_has_spatial_filtering_profile(observation_id);
CREATE INDEX observation_has_spatial_filtering_profile_FKIndex2 ON observation_has_spatial_filtering_profile(spatial_filtering_profile_id);
CREATE INDEX observation_has_geometry_value_FKIndex1 ON observation_has_geometry_value(observation_id);
CREATE INDEX observation_has_geometry_value_FKIndex2 ON observation_has_geometry_value(geometry_value_id);

-----------------------------------------------------------

ALTER TABLE request ADD FOREIGN KEY (offering_id) REFERENCES offering(offering_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE procedure ADD FOREIGN KEY (procedure_description_format_id) REFERENCES procedure_description_format(procedure_description_format_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE sensor_system ADD FOREIGN KEY (parent_sensor_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE sensor_system ADD FOREIGN KEY (child_sensor_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE feature_of_interest ADD FOREIGN KEY (feature_of_interest_type_id) REFERENCES feature_of_interest_type(feature_of_interest_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation ADD FOREIGN KEY (unit_id) REFERENCES unit(unit_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE offering_has_allowed_observation_type ADD FOREIGN KEY (offering_id) REFERENCES offering(offering_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE offering_has_allowed_observation_type ADD FOREIGN KEY (observation_type_id) REFERENCES observation_type(observation_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE procedure_has_observation_type ADD FOREIGN KEY (procedure_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE procedure_has_observation_type ADD FOREIGN KEY (observation_type_id) REFERENCES observation_type(observation_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE quality ADD FOREIGN KEY (swe_type_id) REFERENCES swe_type(swe_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE quality ADD FOREIGN KEY (unit_id) REFERENCES unit(unit_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE offering_has_related_feature ADD FOREIGN KEY (offering_id) REFERENCES offering(offering_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE offering_has_related_feature ADD FOREIGN KEY (related_feature_id) REFERENCES related_feature(related_feature_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE feature_relation ADD FOREIGN KEY (parent_feature_id) REFERENCES feature_of_interest(feature_of_interest_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE feature_relation ADD FOREIGN KEY (child_feature_id) REFERENCES feature_of_interest(feature_of_interest_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE procedure_has_feature_of_interest_type ADD FOREIGN KEY (procedure_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE procedure_has_feature_of_interest_type ADD FOREIGN KEY (feature_of_interest_type_id) REFERENCES feature_of_interest_type(feature_of_interest_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_template ADD FOREIGN KEY (request_id) REFERENCES request(request_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_template ADD FOREIGN KEY (procedure_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE request_has_composite_phenomenon ADD FOREIGN KEY (request_id) REFERENCES request(request_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE request_has_composite_phenomenon ADD FOREIGN KEY (composite_phenomenon_id) REFERENCES composite_phenomenon(composite_phenomenon_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE request_has_observable_property ADD FOREIGN KEY (request_id) REFERENCES request(request_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE request_has_observable_property ADD FOREIGN KEY (observable_property_id) REFERENCES observable_property(observable_property_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_constellation ADD FOREIGN KEY (offering_id) REFERENCES offering(offering_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_constellation ADD FOREIGN KEY (procedure_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_constellation ADD FOREIGN KEY (observable_property_id) REFERENCES observable_property(observable_property_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_constellation ADD FOREIGN KEY (result_type_id) REFERENCES result_type(result_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_constellation ADD FOREIGN KEY (observation_type_id) REFERENCES observation_type(observation_type_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE valid_procedure_time ADD FOREIGN KEY (procedure_id) REFERENCES procedure(procedure_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE composite_phenomenon_has_observable_property ADD FOREIGN KEY (composite_phenomenon_id) REFERENCES composite_phenomenon(Composite_Phenomenon_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE composite_phenomenon_has_observable_property ADD FOREIGN KEY (observable_property_id) REFERENCES observable_property(Observable_Property_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation ADD FOREIGN KEY (observation_constellation_id) REFERENCES observation_constellation(observation_constellation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation ADD FOREIGN KEY (feature_of_interest_id) REFERENCES feature_of_interest(feature_of_interest_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_text_value ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_text_value ADD FOREIGN KEY (text_value_id) REFERENCES text_value(text_value_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE result_template ADD FOREIGN KEY (observation_constellation_id) REFERENCES observation_constellation(observation_constellation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE result_template ADD FOREIGN KEY (feature_of_interest_id) REFERENCES feature_of_interest(feature_of_interest_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_numeric_value ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_numeric_value ADD FOREIGN KEY (numeric_value_id) REFERENCES numeric_value(numeric_value_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_count_value ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_count_value ADD FOREIGN KEY (count_value_id) REFERENCES count_value(count_value_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_boolean_value ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_boolean_value ADD FOREIGN KEY (boolean_value_id) REFERENCES boolean_value(boolean_value_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_category_value ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_category_value ADD FOREIGN KEY (category_value_id) REFERENCES category_value(category_value_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_quality ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_quality ADD FOREIGN KEY (quality_id) REFERENCES quality(quality_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_spatial_filtering_profile ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_spatial_filtering_profile ADD FOREIGN KEY (spatial_filtering_profile_id) REFERENCES spatial_filtering_profile(spatial_filtering_profile_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_geometry_value ADD FOREIGN KEY (observation_id) REFERENCES observation(observation_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE observation_has_geometry_value ADD FOREIGN KEY (geometry_value_id) REFERENCES geometry_value(geometry_value_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE related_feature_has_related_feature_role ADD FOREIGN KEY (related_feature_id) REFERENCES related_feature(related_feature_id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE related_feature_has_related_feature_role ADD FOREIGN KEY (related_feature_role_id) REFERENCES related_feature_role(related_feature_role_id) ON DELETE NO ACTION ON UPDATE NO ACTION;