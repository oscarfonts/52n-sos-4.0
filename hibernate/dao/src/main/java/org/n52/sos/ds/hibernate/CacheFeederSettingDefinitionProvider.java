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
package org.n52.sos.ds.hibernate;

import java.util.Collections;
import java.util.Set;

import org.n52.sos.config.SettingDefinition;
import org.n52.sos.config.SettingDefinitionProvider;
import org.n52.sos.config.settings.IntegerSettingDefinition;
import org.n52.sos.service.ServiceSettings;
import org.n52.sos.util.CollectionHelper;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class CacheFeederSettingDefinitionProvider implements SettingDefinitionProvider {

    public static final String CACHE_THREAD_COUNT = "service.cacheThreadCount";
    
    public static final IntegerSettingDefinition CACHE_THREAD_COUNT_DEFINITION = new IntegerSettingDefinition()
            .setGroup(ServiceSettings.GROUP)
            .setOrder(8)
            .setKey(CACHE_THREAD_COUNT)
            .setDefaultValue(5)
            .setTitle("Cache Feeder Threads")
            .setDescription("The number of threads used to fill the capabilities cache.");
    private static final Set<SettingDefinition<?, ?>> DEFINITIONS = CollectionHelper.<SettingDefinition<?, ?>>set(
            CACHE_THREAD_COUNT_DEFINITION);

    @Override
    public Set<SettingDefinition<?, ?>> getSettingDefinitions() {
        return Collections.unmodifiableSet(DEFINITIONS);
    }
}
