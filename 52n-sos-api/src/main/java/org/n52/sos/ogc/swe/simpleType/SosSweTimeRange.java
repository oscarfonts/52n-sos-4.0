/**
 * Copyright (C) 2012
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
package org.n52.sos.ogc.swe.simpleType;

import org.joda.time.DateTime;
import org.n52.sos.ogc.swe.RangeValue;
import org.n52.sos.ogc.swe.SWEConstants.SweSimpleType;


/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class SosSweTimeRange extends SosSweAbstractUomType<RangeValue<DateTime>> {
    
    private RangeValue<DateTime> value;
    

    /**
     * SWE simple type type
     */
    private SweSimpleType simpleType = SweSimpleType.TimeRange;

    @Override
    public SweSimpleType getSimpleType() {
        return simpleType;
    }

    @Override
    public RangeValue<DateTime> getValue() {
        return value;
    }

    @Override
    public String getStringValue() {
        StringBuilder builder = new StringBuilder();
        if (value.isSetStartValue()) {
            builder.append(value.getRangeStart());
        }
        if (value.isSetEndValue()) {
            builder.append(value.getRangeEnd());
        }
        return builder.toString();
    }

    @Override
    public boolean isSetValue() {
        return value != null && value.isSetStartValue() && value.isSetEndValue();
    }

    @Override
    public void setValue(RangeValue<DateTime> value) {
        this.value = value;
    }

}
