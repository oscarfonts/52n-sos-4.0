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
package org.n52.sos.ogc.swes;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
@SuppressWarnings("rawtypes")
public class SwesExtensions {

    private Set<SwesExtension> extensions = new HashSet<SwesExtension>(0);

    /**
     * 
     * @param extensionName
     *
     * @return
     */
    public boolean isBooleanExtensionSet(String extensionName)    {
        for (SwesExtension<?> swesExtension : extensions) {
            if (swesExtension.getDefinition().equals(extensionName)) {
                Object value = swesExtension.getValue();
                if (value instanceof Boolean) {
                    return (Boolean)value;
                }
                return false;
            }
        }
        return false;
    }

    public void addSwesExtension(SwesExtension<?> extension) {
        extensions.add(extension);
    }
    
}
