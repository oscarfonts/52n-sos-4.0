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
package org.n52.sos.ds.hibernate.entities;

// Generated 10.07.2012 15:18:23 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;

/**
 * FeatureOfInterest generated by hbm2java
 */
public class FeatureOfInterest implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private long featureOfInterestId;

    private FeatureOfInterestType featureOfInterestType;

    private String identifier;
    
    private String name;

    private Geometry geom;

    private String descriptionXml;

    private String url;

//    private Set<Observation> observations = new HashSet<Observation>(0);

    private Set<FeatureOfInterest> featureOfInterestsForChildFeatureId = new HashSet<FeatureOfInterest>(0);

    private Set<FeatureOfInterest> featureOfInterestsForParentFeatureId = new HashSet<FeatureOfInterest>(0);

//    private Set<ResultTemplate> resultTemplates = new HashSet<ResultTemplate>(0);

    public FeatureOfInterest() {
    }

//    public FeatureOfInterest(long featureOfInterestId, FeatureOfInterestType featureOfInterestType) {
//        this.featureOfInterestId = featureOfInterestId;
//        this.featureOfInterestType = featureOfInterestType;
//    }
//
//    public FeatureOfInterest(long featureOfInterestId, FeatureOfInterestType featureOfInterestType, String identifier, String name,
//            Geometry geom, String descriptionXml, String url, Set<Observation> observations,
//            Set<FeatureOfInterest> featureOfInterestsForChildFeatureId, Set<FeatureOfInterest> featureOfInterestsForParentFeatureId, Set<ResultTemplate> resultTemplates) {
//        this.featureOfInterestId = featureOfInterestId;
//        this.featureOfInterestType = featureOfInterestType;
//        this.identifier = identifier;
//        this.name = name;
//        this.geom = geom;
//        this.descriptionXml = descriptionXml;
//        this.url = url;
//        this.observations = observations;
//        this.featureOfInterestsForChildFeatureId = featureOfInterestsForChildFeatureId;
//        this.featureOfInterestsForParentFeatureId = featureOfInterestsForParentFeatureId;
//        this.resultTemplates = resultTemplates;
//    }

    public long getFeatureOfInterestId() {
        return this.featureOfInterestId;
    }

    public void setFeatureOfInterestId(long featureOfInterestId) {
        this.featureOfInterestId = featureOfInterestId;
    }

    public FeatureOfInterestType getFeatureOfInterestType() {
        return this.featureOfInterestType;
    }

    public void setFeatureOfInterestType(FeatureOfInterestType featureOfInterestType) {
        this.featureOfInterestType = featureOfInterestType;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Geometry getGeom() {
        return this.geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public String getDescriptionXml() {
        return this.descriptionXml;
    }

    public void setDescriptionXml(String descriptionXml) {
        this.descriptionXml = descriptionXml;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public Set<Observation> getObservations() {
//        return this.observations;
//    }
//
//    public void setObservations(Set<Observation> observations) {
//        this.observations = observations;
//    }

    public Set<FeatureOfInterest> getFeatureOfInterestsForChildFeatureId() {
        return this.featureOfInterestsForChildFeatureId;
    }

    public void setFeatureOfInterestsForChildFeatureId(Set<FeatureOfInterest> featureOfInterestsForChildFeatureId) {
        this.featureOfInterestsForChildFeatureId = featureOfInterestsForChildFeatureId;
    }

    public Set<FeatureOfInterest> getFeatureOfInterestsForParentFeatureId() {
        return this.featureOfInterestsForParentFeatureId;
    }

    public void setFeatureOfInterestsForParentFeatureId(Set<FeatureOfInterest> featureOfInterestsForParentFeatureId) {
        this.featureOfInterestsForParentFeatureId = featureOfInterestsForParentFeatureId;
    }

//    public Set<ResultTemplate> getResultTemplates() {
//        return this.resultTemplates;
//    }
//
//    public void setResultTemplates(Set<ResultTemplate> resultTemplates) {
//        this.resultTemplates = resultTemplates;
//    }

}
