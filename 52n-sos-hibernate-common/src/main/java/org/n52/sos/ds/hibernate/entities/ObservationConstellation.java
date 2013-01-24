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

// Generated 19.09.2012 15:09:37 by Hibernate Tools 3.4.0.CR1


/**
 * ObservationConstellation generated by hbm2java
 */
public class ObservationConstellation implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private long observationConstellationId;

    private ObservationType observationType;

    private ObservableProperty observableProperty;

    private Procedure procedure;

    private Offering offering;

//    private ResultType resultType;
//
//    private Set<ResultTemplate> resultTemplates = new HashSet<ResultTemplate>(0);
//
//    private Set<Observation> observations = new HashSet<Observation>(0);

    public ObservationConstellation() {
    }

//    public ObservationConstellation(long observationConstellationId,
//            ObservableProperty observableProperty, Procedure procedure, Offering offering) {
//        this.observationConstellationId = observationConstellationId;
//        this.observableProperty = observableProperty;
//        this.procedure = procedure;
//        this.offering = offering;
//    }
//
//    public ObservationConstellation(long observationConstellationId, ObservationType observationType,
//            ObservableProperty observableProperty, Procedure procedure, Offering offering, ResultType resultType,
//            Set<ResultTemplate> resultTemplates, Set<Observation> observations) {
//        this.observationConstellationId = observationConstellationId;
//        this.observationType = observationType;
//        this.observableProperty = observableProperty;
//        this.procedure = procedure;
//        this.offering = offering;
//        this.resultType = resultType;
//        this.resultTemplates = resultTemplates;
//        this.observations = observations;
//    }

    public long getObservationConstellationId() {
        return this.observationConstellationId;
    }

    public void setObservationConstellationId(long observationConstellationId) {
        this.observationConstellationId = observationConstellationId;
    }

    public ObservationType getObservationType() {
        return this.observationType;
    }

    public void setObservationType(ObservationType observationType) {
        this.observationType = observationType;
    }

    public ObservableProperty getObservableProperty() {
        return this.observableProperty;
    }

    public void setObservableProperty(ObservableProperty observableProperty) {
        this.observableProperty = observableProperty;
    }

    public Procedure getProcedure() {
        return this.procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    public Offering getOffering() {
        return this.offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

//    public ResultType getResultType() {
//        return this.resultType;
//    }
//
//    public void setResultType(ResultType resultType) {
//        this.resultType = resultType;
//    }
//
//    public Set<ResultTemplate> getResultTemplates() {
//        return this.resultTemplates;
//    }
//
//    public void setResultTemplates(Set<ResultTemplate> resultTemplates) {
//        this.resultTemplates = resultTemplates;
//    }
//
//    public Set<Observation> getObservations() {
//        return this.observations;
//    }
//
//    public void setObservations(Set<Observation> observations) {
//        this.observations = observations;
//    }

}
