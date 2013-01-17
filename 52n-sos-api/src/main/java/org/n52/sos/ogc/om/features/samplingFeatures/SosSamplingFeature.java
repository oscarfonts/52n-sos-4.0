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
package org.n52.sos.ogc.om.features.samplingFeatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.sos.ogc.OGCConstants;
import org.n52.sos.ogc.gml.CodeType;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.om.features.SosAbstractFeature;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Abstract super class for all sampling features
 * 
 * 
 */
public class SosSamplingFeature extends SosAbstractFeature {
    
    private static final long serialVersionUID = 4660755526492323288L;

    private List<CodeType> name = new LinkedList<CodeType>();

    private String description;

    private String xmlDescription;

    private Geometry geometry;

    private int epsgCode;

    private String featureType = OGCConstants.UNKNOWN;

    private String url;

    private List<SosAbstractFeature> sampledFeatures;

    /**
     * constructor
     * 
     * @param featureIdentifier
     *            identifier of sampling feature
     */
    public SosSamplingFeature(CodeWithAuthority featureIdentifier) {
        super(featureIdentifier);
    }

    public SosSamplingFeature(CodeWithAuthority featureIdentifier, String gmlId) {
       super(featureIdentifier, gmlId);
    }

    public List<CodeType> getName() {
        return Collections.unmodifiableList(name);
    }

    public void setName(List<CodeType> name) {
        this.name = name;
    }
    
    public void addName(CodeType name) {
        if (this.name == null) {
            this.name = new LinkedList<CodeType>();
        }
        this.name.add(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getXmlDescription() {
        return xmlDescription;
    }

    public void setXmlDescription(String xmlDescription) {
        this.xmlDescription = xmlDescription;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public int getEpsgCode() {
        return epsgCode;
    }

    public void setEpsgCode(int epsgCode) {
        this.epsgCode = epsgCode;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSampledFeatures(List<SosAbstractFeature> sampledFeatures) {
        this.sampledFeatures = sampledFeatures;
    }

    public List<SosAbstractFeature> getSampledFeatures() {
        if (isSetSampledFeatures()) {
            return Collections.unmodifiableList(sampledFeatures);
        }
        return new ArrayList<SosAbstractFeature>(0);
    }

    public boolean isSetNames() {
        return name != null && !name.isEmpty();
    }
    
    public CodeType getFirstName() {
        if (isSetNames()) {
            return name.get(0);
        }
        return null;
    }
    
    public boolean isSetSampledFeatures() {
        return sampledFeatures != null && !sampledFeatures.isEmpty();
    }
    
    public boolean isSetUrl() {
        return url != null && !url.isEmpty();
    }

    public boolean isSetGeometry() {
        return geometry != null && !geometry.isEmpty();
    }
    
}
