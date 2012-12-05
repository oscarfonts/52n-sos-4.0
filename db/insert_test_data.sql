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

CREATE OR REPLACE FUNCTION get_observation_type(text) RETURNS bigint AS
$$
	SELECT observation_type_id FROM observation_type 
	WHERE observation_type = 'http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_'::text || $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_procedure(text) RETURNS bigint AS
$$
	SELECT procedure_id FROM procedure WHERE identifier = $1; 
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_feature_of_interest_type(text) RETURNS bigint AS
$$
	SELECT feature_of_interest_type_id 
	FROM feature_of_interest_type 
	WHERE feature_of_interest_type = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_spatial_sampling_feature_type(text) RETURNS bigint AS
$$
	SELECT get_feature_of_interest_type('http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_Sampling'::text || $1);
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_feature_of_interest(text) RETURNS bigint AS
$$
	SELECT feature_of_interest_id FROM feature_of_interest WHERE identifier = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_sensor_ml_description_format() RETURNS bigint AS
$$
	SELECT procedure_description_format_id FROM procedure_description_format 
	WHERE procedure_description_format = 'http://www.opengis.net/sensorML/1.0.1'::text;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_offering(text) RETURNS bigint AS
$$
	SELECT offering_id FROM offering WHERE identifier = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_observable_property(text) RETURNS bigint AS
$$ 
	SELECT observable_property_id FROM observable_property WHERE identifier = $1;
$$
LANGUAGE 'sql';

---- INSERTION FUNCTIONS
CREATE OR REPLACE FUNCTION insert_category_value(text) RETURNS bigint AS
$$
	INSERT INTO category_value(value) SELECT $1 WHERE $1 NOT IN (SELECT value FROM category_value);
	SELECT category_value_id FROM category_value WHERE value = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_count_value(int) RETURNS bigint AS
$$
	INSERT INTO count_value(value) SELECT $1 WHERE $1 NOT IN (SELECT value FROM count_value);
	SELECT count_value_id FROM count_value WHERE value = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_numeric_value(numeric) RETURNS bigint AS
$$
	INSERT INTO numeric_value(value) SELECT $1 WHERE $1 NOT IN (SELECT value FROM numeric_value);
	SELECT numeric_value_id FROM numeric_value WHERE value = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_text_value(text) RETURNS bigint AS
$$
	INSERT INTO text_value(value) SELECT $1 WHERE $1 NOT IN (SELECT value FROM text_value);
	SELECT text_value_id FROM text_value WHERE value = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_geometry_value(geometry) RETURNS bigint AS
$$
	INSERT INTO geometry_value(value) SELECT $1 WHERE $1 NOT IN (SELECT value FROM geometry_value);
	SELECT geometry_value_id FROM geometry_value WHERE value = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_observation_type(text) RETURNS bigint AS
$$
	INSERT INTO observation_type(observation_type) SELECT $1 WHERE $1 NOT IN (SELECT observation_type FROM observation_type);
	SELECT observation_type_id FROM observation_type WHERE observation_type = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_feature_of_interest_type(text) RETURNS bigint AS
$$
	INSERT INTO feature_of_interest_type(feature_of_interest_type) SELECT $1 WHERE $1 NOT IN (SELECT feature_of_interest_type FROM feature_of_interest_type);
	SELECT feature_of_interest_type_id FROM feature_of_interest_type WHERE feature_of_interest_type = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_offering(text) RETURNS bigint AS
$$
	INSERT INTO offering(identifier, name) SELECT $1, $1 WHERE $1 NOT IN (SELECT identifier FROM offering);
	SELECT offering_id FROM offering WHERE identifier = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_unit(text) RETURNS bigint AS
$$
	INSERT INTO unit(unit) SELECT $1 WHERE $1 NOT IN (SELECT unit FROM unit);
	SELECT unit_id FROM unit WHERE unit = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_procedure_description_format(text) RETURNS bigint AS
$$
	INSERT INTO procedure_description_format(procedure_description_format) SELECT $1 WHERE $1 NOT IN (SELECT procedure_description_format FROM procedure_description_format);
	SELECT procedure_description_format_id FROM procedure_description_format WHERE procedure_description_format = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_feature_of_interest(text, numeric, numeric) RETURNS bigint AS
$$
	INSERT INTO feature_of_interest(feature_of_interest_type_id, identifier, name, geom, description_xml) 
	SELECT get_spatial_sampling_feature_type('Point'), $1, $1, ST_GeomFromText('POINT(' || $2 || $3 || ')', 4326), 
'<sams:SF_SpatialSamplingFeature 
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:sams="http://www.opengis.net/samplingSpatial/2.0" 
	xmlns:sf="http://www.opengis.net/sampling/2.0" 
	xmlns:gml="http://www.opengis.net/gml/3.2" gml:id="ssf_'::text || $1 || '">
	<gml:identifier codeSpace="">'::text || $1 || '</gml:identifier>
	<sf:type xlink:href="http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint"/>
	<sf:sampledFeature xlink:href="http://www.opengis.net/def/nil/OGC/0/unknown"/>
	<sams:shape>
		<gml:Point gml:id="p_ssf_'::text || $1 || '">
			<gml:pos srsName="http://www.opengis.net/def/crs/EPSG/0/4326">'::text|| $3 || ' '::text || $2 || '</gml:pos>
		</gml:Point>
	</sams:shape>
</sams:SF_SpatialSamplingFeature>'::text
	WHERE $1 NOT IN (SELECT identifier FROM feature_of_interest);
	SELECT feature_of_interest_id FROM feature_of_interest WHERE identifier = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_observable_property(text) RETURNS bigint AS
$$
	INSERT INTO observable_property(identifier, description) SELECT $1, $1
	WHERE $1 NOT IN (SELECT identifier FROM observable_property WHERE identifier = $1);
	SELECT observable_property_id FROM observable_property WHERE identifier = $1
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION create_sensor_description(text, text, numeric, numeric, numeric) RETURNS text AS
$$
	SELECT 
'<sml:SensorML version="1.0.1"
  xmlns:sml="http://www.opengis.net/sensorML/1.0.1"
  xmlns:gml="http://www.opengis.net/gml"
  xmlns:swe="http://www.opengis.net/swe/1.0.1"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <sml:member>
    <sml:System >
      <sml:identification>
        <sml:IdentifierList>
          <sml:identifier name="uniqueID">
            <sml:Term definition="urn:ogc:def:identifier:OGC:uniqueID">
              <sml:value>'::text || $1 || '</sml:value>
            </sml:Term>
          </sml:identifier>
        </sml:IdentifierList>
      </sml:identification>
      <sml:position name="sensorPosition">
        <swe:Position referenceFrame="urn:ogc:def:crs:EPSG::4326">
          <swe:location>
            <swe:Vector gml:id="STATION_LOCATION">
              <swe:coordinate name="easting">
                <swe:Quantity axisID="x">
                  <swe:uom code="degree"/>
                  <swe:value>'::text || $3 || '</swe:value>
                </swe:Quantity>
              </swe:coordinate>
              <swe:coordinate name="northing">
                <swe:Quantity axisID="y">
                  <swe:uom code="degree"/>
                  <swe:value>'::text || $4 || '</swe:value>
                </swe:Quantity>
              </swe:coordinate>
              <swe:coordinate name="altitude">
                <swe:Quantity axisID="z">
                  <swe:uom code="m"/>
                  <swe:value>'::text || $5 || '</swe:value>
                </swe:Quantity>
              </swe:coordinate>
            </swe:Vector>
          </swe:location>
        </swe:Position>
      </sml:position>
      <sml:inputs>
        <sml:InputList>
          <sml:input name="">
            <swe:ObservableProperty definition="'::text || $2 || '"/>
          </sml:input>
        </sml:InputList>
      </sml:inputs>
      <sml:outputs>
        <sml:OutputList>
          <sml:output name="">
            <swe:Quantity  definition="'::text || $2 || '">
              <swe:uom code="NOT_DEFINED"/>
            </swe:Quantity>
          </sml:output>
        </sml:OutputList>
      </sml:outputs>
      <sml:components xsi:nil="true"/>
    </sml:System>
  </sml:member>
</sml:SensorML>'::text;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_procedure(text,timestamp,text,numeric,numeric,numeric,bigint,bigint) RETURNS bigint AS
$$
	INSERT INTO procedure(identifier, procedure_description_format_id, deleted) SELECT 
		$1, get_sensor_ml_description_format(), false WHERE $1 NOT IN (
			SELECT identifier FROM procedure WHERE identifier = $1);
	INSERT INTO valid_procedure_time(procedure_id, start_time, description_xml) 
		SELECT get_procedure($1), $2, create_sensor_description($1, $3, $4, $5, $6)
		WHERE get_procedure($1) NOT IN (
			SELECT procedure_id FROM valid_procedure_time WHERE procedure_id = get_procedure($1));
	INSERT INTO procedure_has_observation_type(procedure_id, observation_type_id) 
		SELECT get_procedure($1), $7 
		WHERE $7 NOT IN (SELECT observation_type_id FROM procedure_has_observation_type 
				 WHERE procedure_id = get_procedure($1) AND observation_type_id = $7);
	INSERT INTO procedure_has_feature_of_interest_type(procedure_id, feature_of_interest_type_id) 
		SELECT get_procedure($1), $8
		WHERE $8 NOT IN (SELECT feature_of_interest_type_id FROM procedure_has_feature_of_interest_type 
				 WHERE procedure_id = get_procedure($1) AND feature_of_interest_type_id = $8);
	SELECT get_procedure($1);
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_procedure(text,timestamp,text,numeric,numeric,numeric,text,text) RETURNS bigint AS
$$
	SELECT insert_procedure($1, $2, $3, $4, $5, $6, get_observation_type($7), get_spatial_sampling_feature_type($8));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_observation_constellation(bigint,bigint,bigint,bigint) RETURNS bigint AS
$$
	INSERT INTO observation_constellation(observation_type_id, procedure_id, offering_id, observable_property_id)
	SELECT $1,$2,$3,$4 WHERE $1 NOT IN (SELECT observation_type_id 
		FROM observation_constellation  WHERE observation_type_id = $1 
		  AND procedure_id = $2 AND offering_id = $3 AND observable_property_id = $4);
	SELECT observation_constellation_id FROM observation_constellation  
		WHERE observation_type_id = $1  AND procedure_id = $2 AND offering_id = $3 AND observable_property_id = $4;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_observation_constellation(text,text,text,text) RETURNS bigint AS
$$
	SELECT insert_observation_constellation(get_observation_type($1), 
		get_procedure($2), get_offering($3), get_observable_property($4));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_observation_constellation(bigint,bigint,bigint,bigint) RETURNS bigint AS
$$
	SELECT observation_constellation_id FROM observation_constellation  
		WHERE observation_type_id = $1  AND procedure_id = $2 AND offering_id = $3 AND observable_property_id = $4;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_observation_constellation(text,text,text,text) RETURNS bigint AS
$$
	SELECT get_observation_constellation(get_observation_type($1), 
		get_procedure($2), get_offering($3), get_observable_property($4));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_unit(text) RETURNS bigint AS
$$
	SELECT unit_id FROM unit WHERE unit = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION get_boolean_value(boolean) RETURNS bigint AS
$$
	SELECT boolean_value_id FROM boolean_value WHERE value = $1;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_numeric_observation(bigint, numeric) RETURNS VOID AS
$$
	INSERT INTO observation_has_numeric_value(observation_id, numeric_value_id) 
		SELECT $1, insert_numeric_value($2) WHERE $1 NOT IN (
			SELECT observation_id FROM observation_has_numeric_value 
			WHERE observation_id = $1 AND numeric_value_id = insert_numeric_value($2));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_observation(bigint,text, text,timestamp) RETURNS bigint AS
$$ 
	INSERT INTO observation(observation_constellation_id, feature_of_interest_id, unit_id, phenomenon_time_start, result_time)
	SELECT $1, get_feature_of_interest($2), get_unit($3), $4, $4 WHERE $1 NOT IN (
		SELECT observation_constellation_id FROM observation
		WHERE observation_constellation_id = $1 AND feature_of_interest_id = get_feature_of_interest($2) 
				AND unit_id = get_unit($3) AND phenomenon_time_start = $4 AND result_time = $4);

	SELECT observation_id FROM observation 
	WHERE feature_of_interest_id = get_feature_of_interest($2)
		AND observation_constellation_id = $1 AND unit_id = get_unit($3) 
		AND phenomenon_time_start = $4;
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_boolean_observation(bigint, boolean) RETURNS VOID AS
$$ 
 	INSERT INTO observation_has_boolean_value(observation_id, boolean_value_id) SELECT $1, get_boolean_value($2)
 	WHERE $1 NOT IN (SELECT observation_id FROM observation_has_boolean_value 
			WHERE observation_id = $1 AND boolean_value_id = get_boolean_value($2));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_count_observation(bigint, int) RETURNS VOID AS
$$ 
 	INSERT INTO observation_has_count_value(observation_id, count_value_id) SELECT $1, insert_count_value($2)
 	WHERE $1 NOT IN (SELECT observation_id FROM observation_has_count_value 
			WHERE observation_id = $1 AND count_value_id = insert_count_value($2));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_text_observation(bigint, text) RETURNS VOID AS
$$ 
 	INSERT INTO observation_has_text_value(observation_id, text_value_id) SELECT $1, insert_text_value($2)
 	WHERE $1 NOT IN (SELECT observation_id FROM observation_has_text_value 
			WHERE observation_id = $1 AND text_value_id = insert_text_value($2));
$$
LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION insert_category_observation(bigint, text) RETURNS VOID AS
$$ 
 	INSERT INTO observation_has_category_value(observation_id, category_value_id) SELECT $1, insert_category_value($2)
	WHERE $1 NOT IN (SELECT observation_id FROM observation_has_category_value 
		WHERE observation_id = $1 AND category_value_id = insert_category_value($2));
$$
LANGUAGE 'sql';

--
-- NOTE: in table observation: the column identifier can be null but is in the unique constraint....
--

---- OBSERVATION_TYPE
SELECT insert_observation_type('http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation');
SELECT insert_observation_type('http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement');
SELECT insert_observation_type('http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_SWEArrayObservation');
SELECT insert_observation_type('http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation');
SELECT insert_observation_type('http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CategoryObservation');
SELECT insert_observation_type('http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation');

---- FEATURE_OF_INTEREST_TYPE
SELECT insert_feature_of_interest_type('http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingCurve');
SELECT insert_feature_of_interest_type('http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingSurface');
SELECT insert_feature_of_interest_type('http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint');
SELECT insert_feature_of_interest_type('http://www.opengis.net/def/nil/OGC/0/unknown');

---- PROCEDURE_DESCRIPTION_FORMAT
SELECT insert_procedure_description_format('http://www.opengis.net/sensorML/1.0.1');

---- INSERT VALUES
INSERT INTO boolean_value(value) SELECT true  WHERE true  NOT IN (SELECT value FROM boolean_value);
INSERT INTO boolean_value(value) SELECT false WHERE false NOT IN (SELECT value FROM boolean_value);

---- OFFERING
SELECT insert_offering('test_offering_1');
SELECT insert_offering('test_offering_2');
SELECT insert_offering('test_offering_3');
SELECT insert_offering('test_offering_4');
SELECT insert_offering('test_offering_5');

---- FEATURE_OF_INTEREST
SELECT insert_feature_of_interest('test_feature_1', 20.401108, 49.594538);
SELECT insert_feature_of_interest('test_feature_2',  8.401108, 52.980090);
SELECT insert_feature_of_interest('test_feature_3', 10.401108, 52.512348);
SELECT insert_feature_of_interest('test_feature_4',  2.401108, 51.594538);
SELECT insert_feature_of_interest('test_feature_5', 21.401108, 52.127812);

---- UNIT
SELECT insert_unit('test_unit_1');
SELECT insert_unit('test_unit_2');
SELECT insert_unit('test_unit_3');
SELECT insert_unit('test_unit_4');
SELECT insert_unit('test_unit_5');

---- OBSERVABLE_PROPERTY
SELECT insert_observable_property('test_observable_property_1');
SELECT insert_observable_property('test_observable_property_2');
SELECT insert_observable_property('test_observable_property_3');
SELECT insert_observable_property('test_observable_property_4');
SELECT insert_observable_property('test_observable_property_5');

-- PROCEDURES
SELECT insert_procedure('http://www.example.org/sensors/101', '2012-11-19 13:00', 
	'test_observable_property_1', 20.401108, 49.594538, 0.0, 'Measurement', 'Point');

SELECT insert_procedure('http://www.example.org/sensors/102', '2012-11-19 13:00', 
	'test_observable_property_2', 20.401108, 49.594538, 0.0, 'CountObservation', 'Point');

SELECT insert_procedure('http://www.example.org/sensors/103', '2012-11-19 13:00', 
	'test_observable_property_3', 20.401108, 49.594538, 0.0, 'TruthObservation', 'Point');

SELECT insert_procedure('http://www.example.org/sensors/104', '2012-11-19 13:00', 
	'test_observable_property_4', 20.401108, 49.594538, 0.0, 'CategoryObservation', 'Point');

SELECT insert_procedure('http://www.example.org/sensors/105', '2012-11-19 13:00', 
	'test_observable_property_5', 20.401108, 49.594538, 0.0, 'TextObservation', 'Point');

-- OBSERVATION_CONSTELLATION
SELECT insert_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 'test_offering_1', 'test_observable_property_1');
SELECT insert_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 'test_offering_2', 'test_observable_property_2');
SELECT insert_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 'test_offering_3', 'test_observable_property_3');
SELECT insert_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 'test_offering_4', 'test_observable_property_4');
SELECT insert_observation_constellation('TextObservation',  'http://www.example.org/sensors/105', 'test_offering_5', 'test_observable_property_5');

-- INSERT OBSERVATIONS
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:00'), 1.2);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:01'), 1.3);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:02'), 1.4);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:03'), 1.5);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:04'), 1.6);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:05'), 1.7);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:06'), 1.8);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:07'), 1.9);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:08'), 2.0);
SELECT insert_numeric_observation(insert_observation(get_observation_constellation('Measurement', 'http://www.example.org/sensors/101', 
	'test_offering_1', 'test_observable_property_1'), 'test_feature_1', 'test_unit_1', '2012-11-19 13:09'), 2.1);

SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:00'), 1);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:01'), 2);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:02'), 3);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:03'), 4);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:04'), 5);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:05'), 6);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:06'), 7);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:07'), 8);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:08'), 9);
SELECT insert_count_observation(insert_observation(get_observation_constellation('CountObservation', 'http://www.example.org/sensors/102', 
	'test_offering_2', 'test_observable_property_2'), 'test_feature_2', 'test_unit_2', '2012-11-19 13:09'), 10);

SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:00'), true);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:01'), false);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:02'), false);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:03'), true);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:04'), false);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:05'), true);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:06'), true);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:07'), false);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:08'), false);
SELECT insert_boolean_observation(insert_observation(get_observation_constellation('TruthObservation', 'http://www.example.org/sensors/103', 
	'test_offering_3', 'test_observable_property_3'), 'test_feature_3', 'test_unit_3', '2012-11-19 13:09'), true);

SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:00'), 'test_category_1');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:01'), 'test_category_2');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:02'), 'test_category_1');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:03'), 'test_category_5');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:04'), 'test_category_4');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:05'), 'test_category_3');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:06'), 'test_category_1');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:07'), 'test_category_2');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:08'), 'test_category_1');
SELECT insert_category_observation(insert_observation(get_observation_constellation('CategoryObservation', 'http://www.example.org/sensors/104', 
	'test_offering_4', 'test_observable_property_4'), 'test_feature_4', 'test_unit_4', '2012-11-19 13:09'), 'test_category_6');

SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:00'), 'test_text_value_0');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:01'), 'test_text_value_1');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:02'), 'test_text_value_3');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:03'), 'test_text_value_4');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:04'), 'test_text_value_5');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:05'), 'test_text_value_6');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:06'), 'test_text_value_7');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:07'), 'test_text_value_8');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:08'), 'test_text_value_9');
SELECT insert_text_observation(insert_observation(get_observation_constellation('TextObservation', 'http://www.example.org/sensors/105', 
	'test_offering_5', 'test_observable_property_5'), 'test_feature_5', 'test_unit_5', '2012-11-19 13:09'), 'test_text_value_10');

DROP FUNCTION create_sensor_description(text, text, numeric, numeric, numeric);
DROP FUNCTION get_boolean_value(boolean);
DROP FUNCTION get_feature_of_interest(text);
DROP FUNCTION get_feature_of_interest_type(text);
DROP FUNCTION get_observable_property(text);
DROP FUNCTION get_observation_constellation(bigint,bigint,bigint,bigint);
DROP FUNCTION get_observation_constellation(text,text,text,text);
DROP FUNCTION get_observation_type(text);
DROP FUNCTION get_offering(text);
DROP FUNCTION get_procedure(text);
DROP FUNCTION get_sensor_ml_description_format();
DROP FUNCTION get_spatial_sampling_feature_type(text);
DROP FUNCTION get_unit(text);
DROP FUNCTION insert_boolean_observation(bigint, boolean);
DROP FUNCTION insert_category_observation(bigint, text);
DROP FUNCTION insert_category_value(text);
DROP FUNCTION insert_count_observation(bigint, int);
DROP FUNCTION insert_count_value(int);
DROP FUNCTION insert_feature_of_interest(text, numeric, numeric);
DROP FUNCTION insert_feature_of_interest_type(text);
DROP FUNCTION insert_geometry_value(geometry);
DROP FUNCTION insert_numeric_observation(bigint, numeric);
DROP FUNCTION insert_numeric_value(numeric);
DROP FUNCTION insert_observable_property(text);
DROP FUNCTION insert_observation(bigint,text, text,timestamp);
DROP FUNCTION insert_observation_constellation(bigint,bigint,bigint,bigint);
DROP FUNCTION insert_observation_constellation(text,text,text,text);
DROP FUNCTION insert_observation_type(text);
DROP FUNCTION insert_offering(text);
DROP FUNCTION insert_procedure_description_format(text);
DROP FUNCTION insert_procedure(text,timestamp,text,numeric,numeric,numeric,bigint,bigint);
DROP FUNCTION insert_procedure(text,timestamp,text,numeric,numeric,numeric,text,text);
DROP FUNCTION insert_text_observation(bigint, text);
DROP FUNCTION insert_text_value(text);
DROP FUNCTION insert_unit(text);