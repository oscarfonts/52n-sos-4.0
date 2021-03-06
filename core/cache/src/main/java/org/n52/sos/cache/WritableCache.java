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
package org.n52.sos.cache;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.joda.time.DateTime;
import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.ogc.sos.SosEnvelope;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.SetMultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;

/**
 * {@code WritableContentCache} that allows the updating of the underlying maps. All basic CRUD operations are
 * supported.
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 4.0.0
 */
public class WritableCache extends ReadableCache implements WritableContentCache {
    private static final Logger LOG = LoggerFactory.getLogger(WritableCache.class);
    private static final long serialVersionUID = 6625851272234063808L;

    /**
     * Creates a {@code TimePeriod} for the specified {@code ITime}.
     *
     * @param time the abstract time
     *
     * @return the period describing the abstract time
     */
    protected static TimePeriod toTimePeriod(Time time) {
        if (time instanceof TimeInstant) {
            final DateTime instant = ((TimeInstant) time).getValue();
            return new TimePeriod(instant, instant);
        } else {
            return (TimePeriod) time;
        }
    }

    @Override
    public void removeResultTemplates(Collection<String> resultTemplates) {
        for (String resultTemplate : resultTemplates) {
            removeResultTemplate(resultTemplate);
        }
    }

    @Override
    public void addEpsgCode(Integer epsgCode) {
        greaterZero("epsgCode", epsgCode);
        LOG.trace("Adding EpsgCode {}", epsgCode);
        getEpsgCodesSet().add(epsgCode);
    }

    @Override
    public void addFeatureOfInterest(String featureOfInterest) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Adding FeatureOfInterest {}", featureOfInterest);
        getFeaturesOfInterestSet().add(featureOfInterest);
    }

    @Override
    public void addObservationIdentifier(String observationIdentifier) {
        notNullOrEmpty("observationIdentifier", observationIdentifier);
        LOG.trace("Adding ObservationIdentifier {}", observationIdentifier);
        getObservationIdentifiersSet().add(observationIdentifier);
    }

    @Override
    public void addProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Adding procedure {}", procedure);
        getProceduresSet().add(procedure);
    }

    @Override
    public void addResultTemplate(String resultTemplate) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        LOG.trace("Adding SosResultTemplate {}", resultTemplate);
        getResultTemplatesSet().add(resultTemplate);
    }

    @Override
    public void addResultTemplates(Collection<String> resultTemplates) {
        noNullValues("resultTemplates", resultTemplates);
        for (String resultTemplate : resultTemplates) {
            addResultTemplate(resultTemplate);
        }
    }

    @Override
    public void addEpsgCodes(Collection<Integer> epsgCodes) {
        noNullValues("epsgCodes", epsgCodes);
        for (Integer epsgCode : epsgCodes) {
            addEpsgCode(epsgCode);
        }
    }

    @Override
    public void addFeaturesOfInterest(Collection<String> featuresOfInterest) {
        noNullValues("featuresOfInterest", featuresOfInterest);
        for (String featureOfInterest : featuresOfInterest) {
            addFeatureOfInterest(featureOfInterest);
        }
    }

    @Override
    public void addObservationIdentifiers(Collection<String> observationIdentifiers) {
        noNullValues("observationIdentifiers", observationIdentifiers);
        for (String observationIdentifier : observationIdentifiers) {
            addObservationIdentifier(observationIdentifier);
        }
    }

    @Override
    public void addProcedures(Collection<String> procedures) {
        noNullValues("procedures", procedures);
        for (String procedure : procedures) {
            addProcedure(procedure);
        }
    }

    @Override
    public void removeObservationIdentifier(String observationIdentifier) {
        notNullOrEmpty("observationIdentifier", observationIdentifier);
        LOG.trace("Removing ObservationIdentifier {}", observationIdentifier);
        getObservationIdentifiersSet().remove(observationIdentifier);
    }

    @Override
    public void removeObservationIdentifiers(Collection<String> observationIdentifiers) {
        noNullValues("observationIdentifiers", observationIdentifiers);
        for (String observationIdentifier : observationIdentifiers) {
            removeObservationIdentifier(observationIdentifier);
        }
    }

    @Override
    public void removeFeatureOfInterest(String featureOfInterest) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Removing FeatureOfInterest {}", featureOfInterest);
        getFeaturesOfInterestSet().remove(featureOfInterest);
    }

    @Override
    public void removeFeaturesOfInterest(Collection<String> featuresOfInterest) {
        noNullValues("featuresOfInterest", featuresOfInterest);
        for (String featureOfInterest : featuresOfInterest) {
            removeFeatureOfInterest(featureOfInterest);
        }
    }

    @Override
    public void removeProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing Procedure {}", procedure);
        getProceduresSet().remove(procedure);
    }

    @Override
    public void removeProcedures(Collection<String> procedures) {
        noNullValues("procedures", procedures);
        for (String procedure : procedures) {
            removeProcedure(procedure);
        }
    }

    @Override
    public void removeResultTemplate(String resultTemplate) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        LOG.trace("Removing SosResultTemplate {}", resultTemplate);
        getResultTemplatesSet().remove(resultTemplate);
    }

    @Override
    public void setObservablePropertiesForCompositePhenomenon(String compositePhenomenon,
                                                              Collection<String> observableProperties) {
        final Set<String> newValue = newSynchronizedSet(observableProperties);
        LOG.trace("Setting ObservableProperties for CompositePhenomenon {} to {}", compositePhenomenon, newValue);
        getObservablePropertiesForCompositePhenomenonsMap().put(compositePhenomenon, newValue);
    }

    @Override
    public void setObservablePropertiesForOffering(String offering, Collection<String> observableProperties) {
        final Set<String> newValue = newSynchronizedSet(observableProperties);
        LOG.trace("Setting ObservableProperties for Offering {} to {}", offering, observableProperties);
        getObservablePropertiesForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setObservablePropertiesForProcedure(String procedure, Collection<String> observableProperties) {
        final Set<String> newValue = newSynchronizedSet(observableProperties);
        LOG.trace("Setting ObservableProperties for Procedure {} to {}", procedure, newValue);
        getObservablePropertiesForProceduresMap().put(procedure, newValue);
    }

    @Override
    public void setObservationTypesForOffering(String offering, Collection<String> observationTypes) {
        final Set<String> newValue = newSynchronizedSet(observationTypes);
        LOG.trace("Setting ObservationTypes for Offering {} to {}", offering, newValue);
        getObservationTypesForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setOfferingsForObservableProperty(String observableProperty, Collection<String> offerings) {
        final Set<String> newValue = newSynchronizedSet(offerings);
        LOG.trace("Setting Offerings for ObservableProperty {} to {}", observableProperty, newValue);
        getOfferingsForObservablePropertiesMap().put(observableProperty, newValue);
    }

    @Override
    public void setOfferingsForProcedure(String procedure, Collection<String> offerings) {
        final Set<String> newValue = newSynchronizedSet(offerings);
        LOG.trace("Setting Offerings for Procedure {} to {}", procedure, newValue);
        getOfferingsForProceduresMap().put(procedure, newValue);
    }

    @Override
    public void setProceduresForFeatureOfInterest(String featureOfInterest,
                                                  Collection<String> proceduresForFeatureOfInterest) {
        final Set<String> newValue = newSynchronizedSet(proceduresForFeatureOfInterest);
        LOG.trace("Setting Procedures for FeatureOfInterest {} to {}", featureOfInterest, newValue);
        getProceduresForFeaturesOfInterestMap().put(featureOfInterest, newValue);
    }

    @Override
    public void setProceduresForObservableProperty(String observableProperty, Collection<String> procedures) {
        final Set<String> newValue = newSynchronizedSet(procedures);
        LOG.trace("Setting Procedures for ObservablePropert {} to {}", observableProperty, procedures);
        getProceduresForObservablePropertiesMap().put(observableProperty, newValue);
    }

    @Override
    public void setProceduresForOffering(String offering, Collection<String> procedures) {
        final Set<String> newValue = newSynchronizedSet(procedures);
        LOG.trace("Setting Procedures for Offering {} to {}", offering, newValue);
        getProceduresForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setRelatedFeaturesForOffering(String offering, Collection<String> relatedFeatures) {
        final Set<String> newValue = newSynchronizedSet(relatedFeatures);
        LOG.trace("Setting Related Features for Offering {} to {}", offering, newValue);
        getRelatedFeaturesForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setResultTemplatesForOffering(String offering, Collection<String> resultTemplates) {
        final Set<String> newValue = newSynchronizedSet(resultTemplates);
        LOG.trace("Setting ResultTemplates for Offering {} to {}", offering, newValue);
        getResultTemplatesForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setRolesForRelatedFeature(String relatedFeature, Collection<String> roles) {
        final Set<String> newValue = newSynchronizedSet(roles);
        LOG.trace("Setting Roles for RelatedFeature {} to {}", relatedFeature, newValue);
        getRolesForRelatedFeaturesMap().put(relatedFeature, newValue);
    }

    @Override
    public void setFeaturesOfInterest(Collection<String> featuresOfInterest) {
        LOG.trace("Setting FeaturesOfInterest");
        getFeaturesOfInterestSet().clear();
        addFeaturesOfInterest(featuresOfInterest);
    }

    @Override
    public void setPhenomenonTime(DateTime minEventTime, DateTime maxEventTime) {
        setMinPhenomenonTime(minEventTime);
        setMaxPhenomenonTime(maxEventTime);
    }

    @Override
    public void setObservationIdentifiers(Collection<String> observationIdentifiers) {
        LOG.trace("Setting ObservationIdentifiers");
        getObservationIdentifiersSet().clear();
        addObservationIdentifiers(observationIdentifiers);
    }

    @Override
    public void setProcedures(Collection<String> procedures) {
        LOG.trace("Setting Procedures");
        getProceduresSet().clear();
        addProcedures(procedures);
    }

    @Override
    public void setMaxPhenomenonTimeForOffering(String offering, DateTime maxTime) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Setting maximal EventTime for Offering {} to {}", offering, maxTime);
        if (maxTime == null) {
            getMaxPhenomenonTimeForOfferingsMap().remove(offering);
        } else {
            getMaxPhenomenonTimeForOfferingsMap().put(offering, maxTime);
        }
    }

    @Override
    public void setMinPhenomenonTimeForOffering(String offering, DateTime minTime) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Setting minimal EventTime for Offering {} to {}", offering, minTime);
        if (minTime == null) {
            getMinPhenomenonTimeForOfferingsMap().remove(offering);
        } else {
            getMinPhenomenonTimeForOfferingsMap().put(offering, minTime);
        }
    }

    @Override
    public void setMaxPhenomenonTimeForProcedure(String procedure, DateTime maxTime) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Setting maximal phenomenon time for procedure {} to {}", procedure, maxTime);
        if (maxTime == null) {
            getMaxPhenomenonTimeForProceduresMap().remove(procedure);
        } else {
            getMaxPhenomenonTimeForProceduresMap().put(procedure, maxTime);
        }
    }

    @Override
    public void setMinPhenomenonTimeForProcedure(String procedure, DateTime minTime) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Setting minimal phenomenon time for procedure {} to {}", procedure, minTime);
        if (minTime == null) {
            getMinPhenomenonTimeForProceduresMap().remove(procedure);
        } else {
            getMinPhenomenonTimeForProceduresMap().put(procedure, minTime);
        }
    }

    @Override
    public void setNameForOffering(String offering, String name) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("name", name);
        LOG.trace("Setting Name of Offering {} to {}", offering, name);
        getNameForOfferingsMap().put(offering, name);

    }

    @Override
    public void setEnvelopeForOffering(String offering, SosEnvelope envelope) {
        LOG.trace("Setting Envelope for Offering {} to {}", offering, envelope);
        getEnvelopeForOfferingsMap().put(offering, copyOf(envelope));
    }

    @Override
    public Set<String> getFeaturesOfInterestWithOffering() {
        return CollectionHelper.unionOfListOfLists(getFeaturesOfInterestForOfferingMap().values());
    }

    @Override
    public void addAllowedObservationTypeForOffering(String offering, String allowedObservationType) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("allowedObservationType", allowedObservationType);
        LOG.trace("Adding AllowedObservationType {} to Offering {}", allowedObservationType, offering);
        getAllowedObservationTypesForOfferingsMap().add(offering, allowedObservationType);
    }

    @Override
    public void addAllowedObservationTypesForOffering(String offering, Collection<String> allowedObservationTypes) {
        notNullOrEmpty("offering", offering);
        noNullValues("allowedObservationTypes", allowedObservationTypes);
        LOG.trace("Adding AllowedObservationTypes {} to Offering {}", allowedObservationTypes, offering);
        getAllowedObservationTypesForOfferingsMap().addAll(offering, allowedObservationTypes);
    }

    @Override
    public void addCompositePhenomenonForOffering(String offering, String compositePhenomenon) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("compositePhenomenon", compositePhenomenon);
        LOG.trace("Adding compositePhenomenon {} to Offering {}", compositePhenomenon, offering);
        getCompositePhenomenonsForOfferingsMap().add(offering, compositePhenomenon);
    }

    @Override
    public void addFeatureOfInterestForOffering(String offering, String featureOfInterest) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Adding featureOfInterest {} to Offering {}", featureOfInterest, offering);
        getFeaturesOfInterestForOfferingMap().add(offering, featureOfInterest);
    }

    @Override
    public void addFeatureOfInterestForResultTemplate(String resultTemplate, String featureOfInterest) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Adding FeatureOfInterest {} to SosResultTemplate {}", featureOfInterest, resultTemplate);
        getFeaturesOfInterestForResultTemplatesMap().add(resultTemplate, featureOfInterest);
    }

    @Override
    public void addFeaturesOfInterestForResultTemplate(String resultTemplate, Collection<String> featuresOfInterest) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        noNullValues("featuresOfInterest", featuresOfInterest);
        LOG.trace("Adding FeatureOfInterest {} to SosResultTemplate {}", featuresOfInterest, resultTemplate);
        getFeaturesOfInterestForResultTemplatesMap().addAll(resultTemplate, featuresOfInterest);
    }

    @Override
    public void addObservablePropertyForCompositePhenomenon(String compositePhenomenon, String observableProperty) {
        notNullOrEmpty("compositePhenomenon", compositePhenomenon);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Adding ObservableProperty {} to CompositePhenomenon {}", observableProperty, compositePhenomenon);
        getObservablePropertiesForCompositePhenomenonsMap().add(compositePhenomenon, observableProperty);
    }

    @Override
    public void addObservablePropertyForOffering(String offering, String observableProperty) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Adding observableProperty {} to offering {}", observableProperty, offering);
        getObservablePropertiesForOfferingsMap().add(offering, observableProperty);
    }

    @Override
    public void addObservablePropertyForProcedure(String procedure, String observableProperty) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Adding observableProperty {} to procedure {}", observableProperty, procedure);
        getObservablePropertiesForProceduresMap().add(procedure, observableProperty);
    }

    @Override
    public void addObservablePropertyForResultTemplate(String resultTemplate, String observableProperty) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Adding observableProperty {} to resultTemplate {}", observableProperty, resultTemplate);
        getObservablePropertiesForResultTemplatesMap().add(resultTemplate, observableProperty);
    }

    @Override
    public void addObservationIdentifierForProcedure(String procedure, String observationIdentifier) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("observableProperty", observationIdentifier);
        LOG.trace("Adding observationIdentifier {} to procedure {}", observationIdentifier, procedure);
        getObservationIdentifiersForProceduresMap().add(procedure, observationIdentifier);
    }

    @Override
    public void addObservationTypesForOffering(String offering, String observationType) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("observationType", observationType);
        LOG.trace("Adding observationType {} to offering {}", observationType, offering);
        getObservationTypesForOfferingsMap().add(offering, observationType);
    }

    @Override
    public void addOfferingForObservableProperty(String observableProperty, String offering) {
        notNullOrEmpty("observableProperty", observableProperty);
        notNullOrEmpty("offering", offering);
        LOG.trace("Adding offering {} to observableProperty {}", offering, observableProperty);
        getOfferingsForObservablePropertiesMap().add(observableProperty, offering);
    }

    @Override
    public void addOfferingForProcedure(String procedure, String offering) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("offering", offering);
        LOG.trace("Adding offering {} to procedure {}", offering, procedure);
        getOfferingsForProceduresMap().add(procedure, offering);
    }

    @Override
    public void addProcedureForFeatureOfInterest(String featureOfInterest, String procedure) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Adding procedure {} to featureOfInterest {}", procedure, featureOfInterest);
        getProceduresForFeaturesOfInterestMap().add(featureOfInterest, procedure);
    }

    @Override
    public void addProcedureForObservableProperty(String observableProperty, String procedure) {
        notNullOrEmpty("featureOfInterest", observableProperty);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Adding procedure {} to observableProperty {}", procedure, observableProperty);
        getProceduresForObservablePropertiesMap().add(observableProperty, procedure);
    }

    @Override
    public void addProcedureForOffering(String offering, String procedure) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Adding procedure {} to offering {}", procedure, offering);
        getProceduresForOfferingsMap().add(offering, procedure);
    }

    @Override
    public void addRelatedFeatureForOffering(String offering, String relatedFeature) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("relatedFeature", relatedFeature);
        LOG.trace("Adding relatedFeature {} to offering {}", relatedFeature, offering);
        getRelatedFeaturesForOfferingsMap().add(offering, relatedFeature);
    }

    @Override
    public void addRelatedFeaturesForOffering(String offering, Collection<String> relatedFeature) {
        notNullOrEmpty("offering", offering);
        noNullValues("relatedFeature", relatedFeature);
        LOG.trace("Adding relatedFeatures {} to offering {}", relatedFeature, offering);
        getRelatedFeaturesForOfferingsMap().addAll(offering, relatedFeature);
    }

    @Override
    public void addResultTemplateForOffering(String offering, String resultTemplate) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("resultTemplate", resultTemplate);
        LOG.trace("Adding resultTemplate {} to offering {}", resultTemplate, offering);
        getResultTemplatesForOfferingsMap().add(offering, resultTemplate);
    }

    @Override
    public void addRoleForRelatedFeature(String relatedFeature, String role) {
        notNullOrEmpty("relatedFeature", relatedFeature);
        notNullOrEmpty("role", role);
        LOG.trace("Adding role {} to relatedFeature {}", role, relatedFeature);
        getRolesForRelatedFeaturesMap().add(relatedFeature, role);
    }

    @Override
    public void removeAllowedObservationTypeForOffering(String offering, String allowedObservationType) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("allowedObservationType", allowedObservationType);
        LOG.trace("Removing allowedObservationType {} from offering {}", allowedObservationType, offering);
        getAllowedObservationTypesForOfferingsMap().removeWithKey(offering, allowedObservationType);
    }

    @Override
    public void removeAllowedObservationTypesForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing allowedObservationTypes for offering {}", offering);
        getAllowedObservationTypesForOfferingsMap().remove(offering);
    }

    @Override
    public void removeCompositePhenomenonForOffering(String offering, String compositePhenomenon) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("compositePhenomenon", compositePhenomenon);
        LOG.trace("Removing compositePhenomenon {} from offering {}", compositePhenomenon, offering);
        getCompositePhenomenonsForOfferingsMap().removeWithKey(offering, compositePhenomenon);
    }

    @Override
    public void removeCompositePhenomenonsForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing compositePhenomenons for offering {}", offering);
        getCompositePhenomenonsForOfferingsMap().remove(offering);
    }

    @Override
    public void removeEnvelopeForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing envelope for offering {}", offering);
        getEnvelopeForOfferingsMap().remove(offering);
    }

    @Override
    public void removeEpsgCode(Integer epsgCode) {
        notNull("epsgCode", epsgCode);
        LOG.trace("Removing epsgCode {}", epsgCode);
        getEpsgCodesSet().remove(epsgCode);
    }

    @Override
    public void removeEpsgCodes(Collection<Integer> epsgCodes) {
        noNullValues("epsgCodes", epsgCodes);
        for (Integer code : epsgCodes) {
            removeEpsgCode(code);
        }
    }

    @Override
    public void removeFeatureOfInterestForOffering(String offering, String featureOfInterest) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Removing featureOfInterest {} from offering {}", featureOfInterest, offering);
        getFeaturesOfInterestForOfferingMap().removeWithKey(offering, featureOfInterest);
    }

    @Override
    public void removeFeatureOfInterestForResultTemplate(String resultTemplate, String featureOfInterest) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Removing featureOfInterest {} from resultTemplate {}", featureOfInterest, resultTemplate);
        getFeaturesOfInterestForResultTemplatesMap().removeWithKey(resultTemplate, featureOfInterest);
    }

    @Override
    public void removeFeaturesOfInterestForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing featuresOfInterest for offering {}", offering);
        getFeaturesOfInterestForOfferingMap().remove(offering);
    }

    @Override
    public void removeFeaturesOfInterestForResultTemplate(String resultTemplate) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        LOG.trace("Removing featuresOfInterest for resultTemplate {}", resultTemplate);
        getFeaturesOfInterestForResultTemplatesMap().remove(resultTemplate);
    }

    @Override
    public void removeMaxPhenomenonTimeForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing maxEventTime for offering {}", offering);
        getMaxPhenomenonTimeForOfferingsMap().remove(offering);
    }

    @Override
    public void removeMinPhenomenonTimeForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing minEventTime for offering {}", offering);
        getMinPhenomenonTimeForOfferingsMap().remove(offering);
    }

    @Override
    public void removeMaxPhenomenonTimeForProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing maxEventTime for procedure {}", procedure);
        getMaxPhenomenonTimeForProceduresMap().remove(procedure);
    }

    @Override
    public void removeMinPhenomenonTimeForProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing minEventTime for procedure {}", procedure);
        getMinPhenomenonTimeForProceduresMap().remove(procedure);
    }

    @Override
    public void removeNameForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing name for offering {}", offering);
        getNameForOfferingsMap().remove(offering);
    }

    @Override
    public void removeObservablePropertiesForCompositePhenomenon(String compositePhenomenon) {
        notNullOrEmpty("offering", compositePhenomenon);
        LOG.trace("Removing name observableProperties compositePhenomenon {}", compositePhenomenon);
        getObservablePropertiesForCompositePhenomenonsMap().remove(compositePhenomenon);
    }

    @Override
    public void removeObservablePropertiesForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing observableProperties for offering {}", offering);
        getObservablePropertiesForOfferingsMap().remove(offering);
    }

    @Override
    public void removeObservablePropertiesForProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing observableProperties for procedure {}", procedure);
        getObservablePropertiesForProceduresMap().remove(procedure);
    }

    @Override
    public void removeObservablePropertiesForResultTemplate(String resultTemplate) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        LOG.trace("Removing observableProperties for resultTemplate {}", resultTemplate);
        getObservablePropertiesForResultTemplatesMap().remove(resultTemplate);
    }

    @Override
    public void removeObservablePropertyForCompositePhenomenon(String compositePhenomenon, String observableProperty) {
        notNullOrEmpty("compositePhenomenon", compositePhenomenon);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Removing observableProperty {} from compositePhenomenon {}", observableProperty, compositePhenomenon);
        getObservablePropertiesForCompositePhenomenonsMap().removeWithKey(compositePhenomenon, observableProperty);
    }

    @Override
    public void removeObservablePropertyForOffering(String offering, String observableProperty) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Removing observableProperty {} from offering {}", observableProperty, offering);
        getObservablePropertiesForOfferingsMap().removeWithKey(offering, observableProperty);
    }

    @Override
    public void removeObservablePropertyForProcedure(String procedure, String observableProperty) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Removing observableProperty {} from procedure {}", observableProperty, procedure);
        getObservablePropertiesForProceduresMap().removeWithKey(procedure, observableProperty);
    }

    @Override
    public void removeObservablePropertyForResultTemplate(String resultTemplate, String observableProperty) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Removing observableProperty {} from resultTemplate {}", observableProperty, resultTemplate);
        getObservablePropertiesForResultTemplatesMap().removeWithKey(resultTemplate, observableProperty);
    }

    @Override
    public void removeObservationIdentifierForProcedure(String procedure, String observationIdentifier) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("observationIdentifier", observationIdentifier);
        LOG.trace("Removing observationIdentifier {} from procedure {}", observationIdentifier, procedure);
        getObservationIdentifiersForProceduresMap().removeWithKey(procedure, observationIdentifier);
    }

    @Override
    public void removeObservationIdentifiersForProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing observationIdentifiers for procedure {}", procedure);
        getObservationIdentifiersForProceduresMap().remove(procedure);
    }

    @Override
    public void removeObservationTypeForOffering(String offering, String observationType) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("observationType", observationType);
        LOG.trace("Removing observationType {} from offering {}", observationType, offering);
        getObservationTypesForOfferingsMap().removeWithKey(offering, observationType);
    }

    @Override
    public void removeObservationTypesForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing observationTypes for offering {}", offering);
        getObservationTypesForOfferingsMap().remove(offering);
    }

    @Override
    public void removeOfferingForObservableProperty(String observableProperty, String offering) {
        notNullOrEmpty("observableProperty", observableProperty);
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing offering {} from observableProperty {}", offering, observableProperty);
        getOfferingsForObservablePropertiesMap().removeWithKey(observableProperty, offering);
    }

    @Override
    public void removeOfferingForProcedure(String procedure, String offering) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing offering {} from procedure {}", offering, procedure);
        getOfferingsForProceduresMap().removeWithKey(procedure, offering);
    }

    @Override
    public void removeOfferingsForObservableProperty(String observableProperty) {
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Removing offerings for observableProperty {}", observableProperty);
        getOfferingsForObservablePropertiesMap().remove(observableProperty);
    }

    @Override
    public void removeOfferingsForProcedure(String procedure) {
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing offering for procedure {}", procedure);
        getOfferingsForProceduresMap().remove(procedure);
    }

    @Override
    public void removeProcedureForFeatureOfInterest(String featureOfInterest, String procedure) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing procedure {} from featureOfInterest {}", procedure, featureOfInterest);
        getProceduresForFeaturesOfInterestMap().removeWithKey(featureOfInterest, procedure);
    }

    @Override
    public void removeProcedureForObservableProperty(String observableProperty, String procedure) {
        notNullOrEmpty("observableProperty", observableProperty);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing procedure {} from observableProperty {}", procedure, observableProperty);
        getProceduresForObservablePropertiesMap().removeWithKey(observableProperty, procedure);
    }

    @Override
    public void removeProcedureForOffering(String offering, String procedure) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing procedure {} from offering {}", procedure, offering);
        getProceduresForOfferingsMap().removeWithKey(offering, procedure);
    }

    @Override
    public void removeProceduresForFeatureOfInterest(String featureOfInterest) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        LOG.trace("Removing procedures for featureOfInterest {}", featureOfInterest);
        getProceduresForFeaturesOfInterestMap().remove(featureOfInterest);
    }

    @Override
    public void removeProceduresForObservableProperty(String observableProperty) {
        notNullOrEmpty("observableProperty", observableProperty);
        LOG.trace("Removing procedures for observableProperty {}", observableProperty);
        getProceduresForObservablePropertiesMap().remove(observableProperty);
    }

    @Override
    public void removeProceduresForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing procedures for offering {}", offering);
        getProceduresForOfferingsMap().remove(offering);
    }

    @Override
    public void removeRelatedFeatureForOffering(String offering, String relatedFeature) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("relatedFeature", relatedFeature);
        LOG.trace("Removing relatedFeature {} from offering {}", relatedFeature, offering);
        getRelatedFeaturesForOfferingsMap().removeWithKey(offering, relatedFeature);
    }

    @Override
    public void removeRelatedFeaturesForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing RelatedFeatures for offering {}", offering);
        getRelatedFeaturesForOfferingsMap().remove(offering);
    }

    @Override
    public void removeResultTemplateForOffering(String offering, String resultTemplate) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("resultTemplate", resultTemplate);
        LOG.trace("Removing resultTemplate {} from offering {}", resultTemplate, offering);
        getResultTemplatesForOfferingsMap().removeWithKey(offering, resultTemplate);
    }

    @Override
    public void removeResultTemplatesForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing ResultTemplates for offering {}", offering);
        getResultTemplatesForOfferingsMap().remove(offering);
    }

    @Override
    public void removeRoleForRelatedFeature(String relatedFeature, String role) {
        notNullOrEmpty("relatedFeature", relatedFeature);
        notNullOrEmpty("role", role);
        LOG.trace("Removing role {} from relatedFeature {}", role, relatedFeature);
        getRolesForRelatedFeaturesMap().removeWithKey(relatedFeature, role);
    }

    @Override
    public void removeRolesForRelatedFeature(String relatedFeature) {
        notNullOrEmpty("relatedFeature", relatedFeature);
        LOG.trace("Removing roles for relatedFeature {}", relatedFeature);
        getRolesForRelatedFeaturesMap().remove(relatedFeature);
    }

    @Override
    public void removeRolesForRelatedFeatureNotIn(Collection<String> relatedFeatures) {
        notNull("relatedFeatures", relatedFeatures);
        Iterator<String> iter = getRolesForRelatedFeaturesMap().keySet().iterator();
        while (iter.hasNext()) {
            if (!relatedFeatures.contains(iter.next())) {
                iter.remove();
            }
        }
    }

    @Override
    public void setAllowedObservationTypeForOffering(String offering, Collection<String> allowedObservationType) {
        notNullOrEmpty("offering", offering);
        final Set<String> newValue = newSynchronizedSet(allowedObservationType);
        LOG.trace("Setting allowedObservationTypes for offering {} to {}", offering, newValue);
        getAllowedObservationTypesForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setCompositePhenomenonsForOffering(String offering, Collection<String> compositePhenomenons) {
        notNullOrEmpty("offering", offering);
        final Set<String> newValue = newSynchronizedSet(compositePhenomenons);
        LOG.trace("Setting compositePhenomenons for offering {} to {}", offering, newValue);
        getCompositePhenomenonsForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void setFeaturesOfInterestForOffering(String offering, Collection<String> featureOfInterest) {
        notNullOrEmpty("offering", offering);
        final Set<String> newValue = newSynchronizedSet(featureOfInterest);
        LOG.trace("Setting featureOfInterest for offering {} to {}", offering, newValue);
        getFeaturesOfInterestForOfferingMap().put(offering, newValue);
    }

    @Override
    public void setGlobalEnvelope(SosEnvelope globalEnvelope) {
        LOG.trace("Global envelope now: '{}'", getGlobalSpatialEnvelope());
        if (globalEnvelope == null) {
            setGlobalSpatialEnvelope(new SosEnvelope(null, getDefaultEPSGCode()));
        } else {
            setGlobalSpatialEnvelope(globalEnvelope);
        }
        LOG.trace("Global envelope updated to '{}' with '{}'", getGlobalSpatialEnvelope(), globalEnvelope);
    }

    @Override
    public void setMaxPhenomenonTime(DateTime maxEventTime) {
        LOG.trace("Setting Maximal EventTime to {}", maxEventTime);
        getGlobalPhenomenonTimeEnvelope().setEnd(maxEventTime);
    }

    @Override
    public void setMinPhenomenonTime(DateTime minEventTime) {
        LOG.trace("Setting Minimal EventTime to {}", minEventTime);
        getGlobalPhenomenonTimeEnvelope().setStart(minEventTime);
    }

    @Override
    public void setObservablePropertiesForResultTemplate(String resultTemplate, Collection<String> observableProperties) {
        notNullOrEmpty("resultTemplate", resultTemplate);
        final Set<String> newValue = newSynchronizedSet(observableProperties);
        LOG.trace("Setting observableProperties for resultTemplate {} to {}", resultTemplate, newValue);
        getObservablePropertiesForResultTemplatesMap().put(resultTemplate, newValue);
    }

    @Override
    public void setObservationIdentifiersForProcedure(String procedure, Collection<String> observationIdentifiers) {
        notNullOrEmpty("procedure", procedure);
        final Set<String> newValue = newSynchronizedSet(observationIdentifiers);
        LOG.trace("Setting observationIdentifiers for procedure {} to {}", procedure, newValue);
        getObservationIdentifiersForProceduresMap().put(procedure, newValue);
    }

    @Override
    public void setProcedureHierarchy(String procedure, Collection<String> parentProcedures) {
        notNullOrEmpty("procedure", procedure);
        noNullOrEmptyValues("parentProcedures", parentProcedures);
        LOG.trace("Setting parentProcedures for procedure {} to {}", procedure, parentProcedures);
        updateHierarchy(procedure, parentProcedures,
                        getParentProceduresForProceduresMap(),
                        getChildProceduresForProceduresMap());
    }

    @Override
    public void setFeatureHierarchy(String featureOfInterest, Collection<String> parentFeatures) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        noNullOrEmptyValues("parentFeatures", parentFeatures);
        LOG.trace("Setting parentFeatures for featureOfInterest {} to {}", featureOfInterest, parentFeatures);
        updateHierarchy(featureOfInterest, parentFeatures,
                        getParentFeaturesForFeaturesOfInterestMap(),
                        getChildFeaturesForFeaturesOfInterestMap());
    }

    /**
     * Updates the specified child/parent hierarchy for the specified parent with the specified childs.
     *
     * @param <T>       the parent/childs type
     * @param parent    the parent to update
     * @param newChilds the new childs
     * @param parents   the parents map
     * @param childs    the childs map
     */
    protected <T> void updateHierarchy(T parent, Collection<T> newChilds, SetMultiMap<T, T> parents, SetMultiMap<T, T> childs) {
        Set<T> newChildSet = newSynchronizedSet(newChilds);
        Set<T> currentParents = parents.put(parent, newChildSet);
        if (currentParents != null) {
            for (T currentParent : currentParents) {
                LOG.trace("Removing child {} from parent {}", parent, currentParent);
                childs.removeWithKey(currentParent, parent);
            }
        }
        for (T child : newChildSet) {
            LOG.trace("Adding parent {} to child {}", parent, child);
            childs.add(child, parent);
        }
    }

    @Override
    public void addParentFeature(String featureOfInterest, String parentFeature) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        notNullOrEmpty("parentFeature", parentFeature);
        LOG.trace("Adding parentFeature {} to featureOfInterest {}", parentFeature, featureOfInterest);
        getParentFeaturesForFeaturesOfInterestMap().add(featureOfInterest, parentFeature);
        getChildFeaturesForFeaturesOfInterestMap().add(parentFeature, featureOfInterest);
    }

    @Override
    public void addParentFeatures(String featureOfInterest, Collection<String> parentFeatures) {
        notNullOrEmpty("featureOfInterest", featureOfInterest);
        noNullOrEmptyValues("parentFeatures", parentFeatures);
        LOG.trace("Adding parentFeature {} to featureOfInterest {}", parentFeatures, featureOfInterest);
        getParentFeaturesForFeaturesOfInterestMap().addAll(featureOfInterest, parentFeatures);
        for (String parentFeature : parentFeatures) {
            getChildFeaturesForFeaturesOfInterestMap().add(parentFeature, featureOfInterest);
        }
    }

    @Override
    public void addParentProcedure(String procedure, String parentProcedure) {
        notNullOrEmpty("procedure", procedure);
        notNullOrEmpty("parentProcedure", parentProcedure);
        LOG.trace("Adding parentProcedure {} to procedure {}", parentProcedure, procedure);
        getParentProceduresForProceduresMap().add(procedure, parentProcedure);
        getChildProceduresForProceduresMap().add(parentProcedure, procedure);
    }

    @Override
    public void addParentProcedures(String procedure, Collection<String> parentProcedures) {
        notNullOrEmpty("procedure", procedure);
        noNullOrEmptyValues("parentProcedures", parentProcedures);
        LOG.trace("Adding parentProcedures {} to procedure {}", parentProcedures, procedure);
        getParentProceduresForProceduresMap().addAll(procedure, parentProcedures);
        for (String parentProcedure : parentProcedures) {
            getChildProceduresForProceduresMap().add(parentProcedure, procedure);
        }
    }

    @Override
    public void updateEnvelopeForOffering(String offering, Envelope envelope) {
        notNullOrEmpty("offering", offering);
        notNull("envelope", envelope);
        if (hasEnvelopeForOffering(offering)) {
            final SosEnvelope offeringEnvelope = getEnvelopeForOfferingsMap().get(offering);
            LOG.trace("Expanding envelope {} for offering {} to include {}", offeringEnvelope, offering, envelope);
            offeringEnvelope.expandToInclude(envelope);
        } else {
            setEnvelopeForOffering(offering, new SosEnvelope(envelope, getDefaultEPSGCode()));
        }
    }

    @Override
    public void updatePhenomenonTime(Time eventTime) {
        notNull("eventTime", eventTime);
        TimePeriod tp = toTimePeriod(eventTime);
        LOG.trace("Expanding global EventTime to include {}", tp);
        if (!hasMinPhenomenonTime() || getMinPhenomenonTime().isAfter(tp.getStart())) {
            setMinPhenomenonTime(tp.getStart());
        }
        if (!hasMaxPhenomenonTime() || getMaxPhenomenonTime().isBefore(tp.getEnd())) {
            setMaxPhenomenonTime(tp.getEnd());
        }
    }

    @Override
    public void updateGlobalEnvelope(Envelope envelope) {
        notNull("envelope", envelope);
        if (hasGlobalEnvelope()) {
            LOG.trace("Expanding envelope {} to include {}", getGlobalSpatialEnvelope(), envelope);
            getGlobalSpatialEnvelope().expandToInclude(envelope);
        } else {
            setGlobalEnvelope(new SosEnvelope(new Envelope(envelope), getDefaultEPSGCode()));
        }
    }

    @Override
    public void updatePhenomenonTimeForOffering(String offering, Time eventTime) {
        notNullOrEmpty("offering", offering);
        notNull("eventTime", eventTime);
        TimePeriod tp = toTimePeriod(eventTime);
        LOG.trace("Expanding EventTime of offering {} to include {}", offering, tp);
        if (!hasMaxPhenomenonTimeForOffering(offering)
            || getMaxPhenomenonTimeForOffering(offering).isBefore(tp.getEnd())) {
            setMaxPhenomenonTimeForOffering(offering, tp.getEnd());
        }
        if (!hasMinPhenomenonTimeForOffering(offering)
            || getMinPhenomenonTimeForOffering(offering).isAfter(tp.getStart())) {
            setMinPhenomenonTimeForOffering(offering, tp.getStart());
        }
    }

    @Override
    public void updatePhenomenonTimeForProcedure(String procedure, Time eventTime) {
        notNullOrEmpty("procedure", procedure);
        notNull("eventTime", eventTime);
        TimePeriod tp = toTimePeriod(eventTime);
        LOG.trace("Expanding phenomenon time of procedure {} to include {}", procedure, tp);
        if (!hasMaxPhenomenonTimeForProcedure(procedure)
            || getMaxPhenomenonTimeForProcedure(procedure).isBefore(tp.getEnd())) {
            setMaxPhenomenonTimeForProcedure(procedure, tp.getEnd());
        }
        if (!hasMinPhenomenonTimeForProcedure(procedure)
            || getMinPhenomenonTimeForProcedure(procedure).isAfter(tp.getStart())) {
            setMinPhenomenonTimeForProcedure(procedure, tp.getStart());
        }
    }

    @Override
    public void recalculateGlobalEnvelope() {
        LOG.trace("Recalculating global spatial envelope based on offerings");
        SosEnvelope globalEnvelope = null;
        if (!getOfferings().isEmpty()) {
            for (String offering : getOfferings()) {
                SosEnvelope e = getEnvelopeForOffering(offering);
                if (e != null) {
                    if (globalEnvelope == null) {
                        if (e.isSetEnvelope()) {
                            globalEnvelope = new SosEnvelope(new Envelope(e.getEnvelope()), e.getSrid());
                            LOG.trace("First envelope '{}' used as starting point", globalEnvelope);
                        }
                    } else {
                        globalEnvelope.getEnvelope().expandToInclude(e.getEnvelope());
                        LOG.trace("Envelope expanded to include '{}' resulting in '{}'", e, globalEnvelope);
                    }
                }
            }
            if (globalEnvelope == null) {
                LOG.error("Global envelope could not be resetted");
            }
        } else {
            globalEnvelope = new SosEnvelope(null, getDefaultEPSGCode());
        }
        setGlobalEnvelope(globalEnvelope);
        LOG.trace("Spatial envelope finally set to '{}'", getGlobalEnvelope());
    }

    @Override
    public void recalculatePhenomenonTime() {
        LOG.trace("Recalculating global event time based on offerings");
        DateTime globalMax = null, globalMin = null;
        if (!getOfferings().isEmpty()) {
            for (String offering : getOfferings()) {
                if (hasMaxPhenomenonTimeForOffering(offering)) {
                    DateTime offeringMax = getMaxPhenomenonTimeForOffering(offering);
                    if (globalMax == null || offeringMax.isAfter(globalMax)) {
                        globalMax = offeringMax;
                    }
                }
                if (hasMinPhenomenonTimeForOffering(offering)) {
                    DateTime offeringMin = getMinPhenomenonTimeForOffering(offering);
                    if (globalMin == null || offeringMin.isBefore(globalMin)) {
                        globalMin = offeringMin;
                    }
                }
            }
            if (globalMin == null || globalMax == null) {
                LOG.error("Error in cache! Reset of global temporal bounding box failed. Max: '{}'); Min: '{}'",
                          globalMax, globalMin);
            }
        }
        setPhenomenonTime(globalMin, globalMax);
        LOG.trace("Global temporal bounding box reset done. Min: '{}'); Max: '{}'",
                  getMinPhenomenonTime(), getMaxPhenomenonTime());
    }

    @Override
    public void removeMaxResultTimeForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing maxResultTime for offering {}", offering);
        getMaxResultTimeForOfferingsMap().remove(offering);
    }

    @Override
    public void removeMinResultTimeForOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing minResultTime for offering {}", offering);
        getMinResultTimeForOfferingsMap().remove(offering);
    }

    @Override
    public void setResultTime(DateTime min, DateTime max) {
        setMinResultTime(min);
        setMaxResultTime(max);
    }

    @Override
    public void updateResultTime(Time resultTime) {
        if (resultTime == null) {
            return;
        }
        TimePeriod tp = toTimePeriod(resultTime);
        LOG.trace("Expanding global ResultTime to include {}", tp);
        if (!hasMinResultTime() || getMinResultTime().isAfter(tp.getStart())) {
            setMinResultTime(tp.getStart());
        }
        if (!hasMaxResultTime() || getMaxResultTime().isBefore(tp.getEnd())) {
            setMaxResultTime(tp.getEnd());
        }
    }

    @Override
    public void recalculateResultTime() {
        LOG.trace("Recalculating global result time based on offerings");
        DateTime globalMax = null, globalMin = null;
        if (!getOfferings().isEmpty()) {
            for (String offering : getOfferings()) {
                if (hasMaxResultTimeForOffering(offering)) {
                    DateTime offeringMax = getMaxResultTimeForOffering(offering);
                    if (globalMax == null || offeringMax.isAfter(globalMax)) {
                        globalMax = offeringMax;
                    }
                }
                if (hasMinResultTimeForOffering(offering)) {
                    DateTime offeringMin = getMinResultTimeForOffering(offering);
                    if (globalMin == null || offeringMin.isBefore(globalMin)) {
                        globalMin = offeringMin;
                    }
                }
            }
        }
        setResultTime(globalMin, globalMax);
        LOG.trace("Global result time bounding box reset done. Min: '{}'); Max: '{}'",
                  getMinResultTime(), getMaxResultTime());
    }

    @Override
    public void setMaxResultTime(DateTime maxResultTime) {
        LOG.trace("Setting Maximal ResultTime to {}", maxResultTime);
        getGlobalPhenomenonTimeEnvelope().setEnd(maxResultTime);
    }

    @Override
    public void setMaxResultTimeForOffering(String offering, DateTime maxTime) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Setting maximal ResultTime for Offering {} to {}", offering, maxTime);
        if (maxTime == null) {
            getMaxResultTimeForOfferingsMap().remove(offering);
        } else {
            getMaxResultTimeForOfferingsMap().put(offering, maxTime);
        }
    }

    @Override
    public void setMinResultTime(DateTime minResultTime) {
        LOG.trace("Setting Minimal ResultTime to {}", minResultTime);
        getGlobalPhenomenonTimeEnvelope().setStart(minResultTime);
    }

    @Override
    public void setMinResultTimeForOffering(String offering, DateTime minTime) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Setting minimal ResultTime for Offering {} to {}", offering, minTime);
        if (minTime == null) {
            getMinResultTimeForOfferingsMap().remove(offering);
        } else {
            getMinResultTimeForOfferingsMap().put(offering, minTime);
        }
    }

    @Override
    public void updateResultTimeForOffering(String offering, Time resultTime) {
        notNullOrEmpty("offering", offering);
        if (resultTime == null) {
            return;
        }
        TimePeriod tp = toTimePeriod(resultTime);
        LOG.trace("Expanding EventTime of offering {} to include {}", offering, tp);
        if (!hasMaxResultTimeForOffering(offering)
            || getMaxResultTimeForOffering(offering).isBefore(tp.getEnd())) {
            setMaxResultTimeForOffering(offering, tp.getEnd());
        }
        if (!hasMinResultTimeForOffering(offering)
            || getMinResultTimeForOffering(offering).isAfter(tp.getStart())) {
            setMinResultTimeForOffering(offering, tp.getStart());
        }
    }

    @Override
    public void clearFeaturesOfInterest() {
        LOG.trace("Clearing features of interest");
        getFeaturesOfInterestSet().clear();
    }

    @Override
    public void clearProceduresForFeatureOfInterest() {
        LOG.trace("Clearing procedures for feature of interest");
        getProceduresForFeaturesOfInterestMap().clear();
    }

    @Override
    public void clearFeatureHierarchy() {
        LOG.trace("Clearing feature hierarchy");
        getChildFeaturesForFeaturesOfInterestMap().clear();
        getParentFeaturesForFeaturesOfInterestMap().clear();
    }

    @Override
    public void clearProceduresForOfferings() {
        LOG.trace("Clearing procedures for offerings");
        getProceduresForOfferingsMap().clear();
    }

    @Override
    public void clearNameForOfferings() {
        LOG.trace("Clearing names for offerings");
        getNameForOfferingsMap().clear();
    }

    @Override
    public void clearObservablePropertiesForOfferings() {
        LOG.trace("Clearing observable properties for offerings");
        getObservablePropertiesForOfferingsMap().clear();
    }

    @Override
    public void clearRelatedFeaturesForOfferings() {
        LOG.trace("Clearing related features for offerings");
        getRelatedFeaturesForOfferingsMap().clear();
    }

    @Override
    public void clearObservationTypesForOfferings() {
        LOG.trace("Clearing observation types for offerings");
        getObservationTypesForOfferingsMap().clear();
    }

    @Override
    public void clearAllowedObservationTypeForOfferings() {
        LOG.trace("Clearing allowed observation types for offerings");
        getAllowedObservationTypesForOfferingsMap().clear();
    }

    @Override
    public void clearEnvelopeForOfferings() {
        LOG.trace("Clearing envelope for offerings");
        getEnvelopeForOfferingsMap().clear();
    }

    @Override
    public void clearFeaturesOfInterestForOfferings() {
        LOG.trace("Clearing features of interest for offerings");
        getFeaturesOfInterestForOfferingMap().clear();
    }

    @Override
    public void clearMinPhenomenonTimeForOfferings() {
        LOG.trace("Clearing min phenomenon time for offerings");
        getMinPhenomenonTimeForOfferingsMap().clear();
    }

    @Override
    public void clearMaxPhenomenonTimeForOfferings() {
        LOG.trace("Clearing max phenomenon time for offerings");
        getMaxPhenomenonTimeForOfferingsMap().clear();
    }

    @Override
    public void clearMinPhenomenonTimeForProcedures() {
        LOG.trace("Clearing min phenomenon time for procedures");
        getMinPhenomenonTimeForProceduresMap().clear();
    }

    @Override
    public void clearMaxPhenomenonTimeForProcedures() {
        LOG.trace("Clearing max phenomenon time for procedures");
        getMaxPhenomenonTimeForProceduresMap().clear();
    }
    
    @Override
    public void clearMinResultTimeForOfferings() {
        LOG.trace("Clearing min result time for offerings");
        getMinResultTimeForOfferingsMap().clear();
    }

    @Override
    public void clearMaxResultTimeForOfferings() {
        LOG.trace("Clearing max result time for offerings");
        getMaxResultTimeForOfferingsMap().clear();
    }

    @Override
    public void clearOfferings() {
        LOG.trace("Clearing offerings");
        getOfferingsSet().clear();
    }

    @Override
    public void addOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Adding offering {}", offering);
        getOfferingsSet().add(offering);
    }

    @Override
    public void setOfferings(Collection<String> offerings) {
        clearOfferings();
        addOfferings(offerings);
    }

    @Override
    public void addOfferings(Collection<String> offerings) {
        noNullValues("offerings", offerings);
        for (String offering : offerings) {
            addOffering(offering);
        }
    }

    @Override
    public void removeOffering(String offering) {
        notNullOrEmpty("offering", offering);
        LOG.trace("Removing Offering {}", offering);
        getOfferingsSet().remove(offering);
    }

    @Override
    public void removeOfferings(Collection<String> offerings) {
        noNullValues("offerings", offerings);
        for (String offering : offerings) {
            removeOffering(offering);
        }
    }

    @Override
    public void addHiddenChildProcedureForOffering(String offering, String procedure) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Adding hidden child procedure {} to offering {}", procedure, offering);
        getHiddenChildProceduresForOfferingsMap().add(offering, procedure);
    }

    @Override
    public void removeHiddenChildProcedureForOffering(String offering, String procedure) {
        notNullOrEmpty("offering", offering);
        notNullOrEmpty("procedure", procedure);
        LOG.trace("Removing hidden chil procedure {} from offering {}", procedure, offering);
        getHiddenChildProceduresForOfferingsMap().removeWithKey(offering, procedure);
    }

    @Override
    public void setHiddenChildProceduresForOffering(String offering, Collection<String> procedures) {
        final Set<String> newValue = newSynchronizedSet(procedures);
        LOG.trace("Setting hidden child Procedures for Offering {} to {}", offering, newValue);
        getHiddenChildProceduresForOfferingsMap().put(offering, newValue);
    }

    @Override
    public void clearHiddenChildProceduresForOfferings() {
        LOG.trace("Clearing hidden child procedures for offerings");
        getHiddenChildProceduresForOfferingsMap().clear();
    }
}
