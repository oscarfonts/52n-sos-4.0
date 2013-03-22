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
package org.n52.sos.ds.hibernate.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Observation implements java.io.Serializable {
    private static final long serialVersionUID = 4419764404575493525L;
    private long observationId;
    private Boolean deleted;
    private FeatureOfInterest featureOfInterest;
    private ObservationConstellation observationConstellation;
    private String identifier;
    private Codespace codespace;
    private Date phenomenonTimeStart;
    private Date phenomenonTimeEnd;
    private Date resultTime;
    private Date validTimeStart;
    private Date validTimeEnd;
    private Unit unit;
    private String setId;
    private Set<ObservationConstellationOfferingObservationType> observationConstellationOfferingObservationTypes =
                                                                 new HashSet<ObservationConstellationOfferingObservationType>(0);
    private Set<SpatialFilteringProfile> spatialFilteringProfiles;
    private Set<Quality> qualities = new HashSet<Quality>(0);

    public Observation() {
    }

    public long getObservationId() {
        return this.observationId;
    }

    public void setObservationId(long observationId) {
        this.observationId = observationId;
    }

    public FeatureOfInterest getFeatureOfInterest() {
        return this.featureOfInterest;
    }

    public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
        this.featureOfInterest = featureOfInterest;
    }

    public ObservationConstellation getObservationConstellation() {
        return this.observationConstellation;
    }

    public void setObservationConstellation(ObservationConstellation observationConstellation) {
        this.observationConstellation = observationConstellation;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Codespace getCodespace() {
        return this.codespace;
    }

    public void setCodespace(Codespace codespace) {
        this.codespace = codespace;
    }
    
    public Date getPhenomenonTimeStart() {
        return this.phenomenonTimeStart;
    }

    public void setPhenomenonTimeStart(Date phenomenonTimeStart) {
        this.phenomenonTimeStart = phenomenonTimeStart;
    }

    public Date getPhenomenonTimeEnd() {
        return this.phenomenonTimeEnd;
    }

    public void setPhenomenonTimeEnd(Date phenomenonTimeEnd) {
        this.phenomenonTimeEnd = phenomenonTimeEnd;
    }

    public Date getResultTime() {
        return this.resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public Date getValidTimeStart() {
        return this.validTimeStart;
    }

    public void setValidTimeStart(Date validTimeStart) {
        this.validTimeStart = validTimeStart;
    }

    public Date getValidTimeEnd() {
        return this.validTimeEnd;
    }

    public void setValidTimeEnd(Date validTimeEnd) {
        this.validTimeEnd = validTimeEnd;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getSetId() {
        return this.setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public Set<ObservationConstellationOfferingObservationType> getObservationConstellationOfferingObservationTypes() {
        return this.observationConstellationOfferingObservationTypes;
    }

    public void setObservationConstellationOfferingObservationTypes(
            Set<ObservationConstellationOfferingObservationType> observationConstellationOfferingObservationTypes) {
        this.observationConstellationOfferingObservationTypes = observationConstellationOfferingObservationTypes;
    }

    public Set<SpatialFilteringProfile> getSpatialFilteringProfiles() {
        return this.spatialFilteringProfiles;
    }

    public void setSpatialFilteringProfiles(Set<SpatialFilteringProfile> spatialFilteringProfiles) {
        this.spatialFilteringProfiles = spatialFilteringProfiles;
    }

    public Set<Quality> getQualities() {
        return this.qualities;
    }

    public void setQualities(Set<Quality> qualities) {
        this.qualities = qualities;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public boolean isSetIdentifier() {
        return getIdentifier() != null && !getIdentifier().isEmpty();
    }
    
    public boolean isSetCodespace() {
        return getCodespace() != null && getCodespace().isSetCodespace();
    }
}
