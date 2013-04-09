/**
 * Copyright (C) 2013 by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk 52 North Initiative for Geospatial Open Source Software GmbH Martin-Luther-King-Weg 24 48155
 * Muenster, Germany info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under the terms of the GNU General Public
 * License version 2 as published by the Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied WARRANTY OF MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program (see gnu-gpl v2.txt). If
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or visit
 * the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.ds.hibernate.entities;

import java.io.Serializable;

import org.n52.sos.ds.hibernate.entities.HibernateRelations.HasFeatureOfInterestType;

public class FeatureOfInterestType implements Serializable {
    public static final String ID = "featureOfInterestTypeId";
    public static final String FEATURE_OF_INTEREST_TYPE = HasFeatureOfInterestType.FEATURE_OF_INTEREST_TYPE;
    private static final long serialVersionUID = -6503982983386540487L;
    private long featureOfInterestTypeId;
    private String featureOfInterestType;

    public FeatureOfInterestType() {
    }

    public long getFeatureOfInterestTypeId() {
        return this.featureOfInterestTypeId;
    }

    public void setFeatureOfInterestTypeId(long featureOfInterestTypeId) {
        this.featureOfInterestTypeId = featureOfInterestTypeId;
    }

    public String getFeatureOfInterestType() {
        return this.featureOfInterestType;
    }

    public void setFeatureOfInterestType(String featureOfInterestType) {
        this.featureOfInterestType = featureOfInterestType;
    }
}
