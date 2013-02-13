/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.config;

import java.net.URI;

import org.n52.sos.config.settings.BooleanSettingDefinition;
import org.n52.sos.config.settings.IntegerSettingDefinition;
import org.n52.sos.config.settings.StringSettingDefinition;
import org.n52.sos.config.settings.UriSettingDefinition;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class ServiceSettingDefinitions {
    
    public static final SettingDefinitionGroup GROUP = new SettingDefinitionGroup()
            .setTitle("Service settings");
    
    public static final ISettingDefinition<URI> SOS_URL = new UriSettingDefinition()
            .setGroup(GROUP)
            .setKey("SOS_URL")
            .setTitle("SOS URL")
            .setDescription("The endpoint URL of this sos which will be shown in the GetCapabilities response "
                            + "(e.g. <code>http://localhost:8080/52nSOS/sos</code>). The path to a specific "
                            + "binding (like <code>/soap</code>) will appended to this URL.");
    public static final ISettingDefinition<Integer> DEFAULT_EPSG = new IntegerSettingDefinition()
            .setGroup(GROUP)
            .setKey("DEFAULT_EPSG")
            .setDefaultValue(4326)
            .setTitle("Default EPSG Code")
            .setDescription("The EPSG code in which the geometries are stored.");
    public static final ISettingDefinition<Integer> LEASE = new IntegerSettingDefinition()
            .setGroup(GROUP)
            .setKey("LEASE")
            .setDefaultValue(600)
            .setTitle("Lease")
            .setDescription("Time of lease for result template in GetResult operation in minutes.");
    public static final ISettingDefinition<Integer> MINIMUM_GZIP_SIZE = new IntegerSettingDefinition()
            .setMinimum(0)
            .setGroup(GROUP)
            .setKey("MINIMUM_GZIP_SIZE")
            .setDefaultValue(1048576)
            .setTitle("GZIP Threshold")
            .setDescription("The size (in byte) the SOS starts to gzip responses (if the client supports it).");
    public static final ISettingDefinition<Integer> MAX_GET_OBSERVATION_RESULTS = new IntegerSettingDefinition()
            .setGroup(GROUP)
            .setKey("MAX_GET_OBSERVATION_RESULTS")
            .setDefaultValue(0)
            .setTitle("Maximum number of observations")
            .setDescription("Maximum number of observation in GetObservation responses. " 
                            + "Set to <code>0</code> (zero) for unlimited number of observations.");
    public static final ISettingDefinition<Boolean> SUPPORTS_QUALITY = new BooleanSettingDefinition()
            .setGroup(GROUP)
            .setKey("SUPPORTS_QUALITY")
            .setDefaultValue(true)
            .setTitle("Supports quality")
            .setDescription("Support quality information in observations.");
    public static final ISettingDefinition<Integer> CAPABILITIES_CACHE_UPDATE_INTERVAL = new IntegerSettingDefinition()
            .setMinimum(0)
            .setGroup(GROUP)
            .setKey("CAPABILITIES_CACHE_UPDATE_INTERVAL")
            .setDefaultValue(5)
            .setTitle("Capabilities cache update interval")
            .setDescription("The update interval of the capabilities cache in minutes.");
    public static final ISettingDefinition<String> SENSOR_DIRECTORY = new StringSettingDefinition()
            .setGroup(GROUP)
            .setKey("SENSOR_DIRECTORY")
            .setOptional(true)
            .setTitle("Sensor Directory")
            .setDescription("The path to a directory with the sensor descriptions in SensorML format. " 
                            + "It can be either an absolute path (like <code>/home/user/sosconfig/sensors</code>) " 
                            + "or a path relative to the web application directory (e.g. <code>WEB-INF/sensors</code>).");
    public static final ISettingDefinition<Integer> CACHE_THREAD_COUNT = new IntegerSettingDefinition()
            .setGroup(GROUP)
            .setKey("CACHE_THREAD_COUNT")
            .setDefaultValue(5)
            .setTitle("Cache Feeder Threads")
            .setDescription("The number of threads used to fill the capabilities cache.");
    public static final ISettingDefinition<Boolean> SKIP_DUPLICATE_OBSERVATIONS = new BooleanSettingDefinition()
            .setGroup(GROUP)
            .setKey("SKIP_DUPLICATE_OBSERVATIONS")
            .setDefaultValue(true)
            .setTitle("Skip duplicate observations")
            .setDescription("Skip duplicate observations silently when inserted by batch.");
    public static final ISettingDefinition<Boolean> CHILD_PROCEDURES_ENCODED_IN_PARENTS_DESCRIBE_SENSOR = new BooleanSettingDefinition()
            .setGroup(GROUP)
            .setKey("CHILD_PROCEDURES_ENCODED_IN_PARENTS_DESCRIBE_SENSOR")
            .setDefaultValue(false)
            .setTitle("Encode child procedures.")
            .setDescription("Encode the complete sensor description of a child procedure in the parent procedure description.");
    public static final ISettingDefinition<String> CONFIGURATION_FILES = new StringSettingDefinition()
            .setGroup(GROUP)
            .setKey("CONFIGURATION_FILES")
            .setOptional(true)
            .setTitle("Configuration Files")
            .setDescription("Configuration files and their file identifier (List: IDENTIFIER FILENAME;IDENTIFIER2 FILENAME2; ...).");
}