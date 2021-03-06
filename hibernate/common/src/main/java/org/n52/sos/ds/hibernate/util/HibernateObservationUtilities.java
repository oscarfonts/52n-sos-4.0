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
package org.n52.sos.ds.hibernate.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.n52.sos.cache.ContentCache;
import org.n52.sos.ds.FeatureQueryHandler;
import org.n52.sos.ds.hibernate.entities.BlobObservation;
import org.n52.sos.ds.hibernate.entities.BooleanObservation;
import org.n52.sos.ds.hibernate.entities.CategoryObservation;
import org.n52.sos.ds.hibernate.entities.CountObservation;
import org.n52.sos.ds.hibernate.entities.FeatureOfInterest;
import org.n52.sos.ds.hibernate.entities.GeometryObservation;
import org.n52.sos.ds.hibernate.entities.NumericObservation;
import org.n52.sos.ds.hibernate.entities.ObservableProperty;
import org.n52.sos.ds.hibernate.entities.Observation;
import org.n52.sos.ds.hibernate.entities.ObservationConstellation;
import org.n52.sos.ds.hibernate.entities.Offering;
import org.n52.sos.ds.hibernate.entities.Procedure;
import org.n52.sos.ds.hibernate.entities.SweDataArrayObservation;
import org.n52.sos.ds.hibernate.entities.TextObservation;
import org.n52.sos.exception.CodedException;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.gml.time.Time;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.ogc.om.AbstractPhenomenon;
import org.n52.sos.ogc.om.MultiObservationValues;
import org.n52.sos.ogc.om.ObservationValue;
import org.n52.sos.ogc.om.OmObservableProperty;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.om.OmObservationConstellation;
import org.n52.sos.ogc.om.SingleObservationValue;
import org.n52.sos.ogc.om.features.AbstractFeature;
import org.n52.sos.ogc.om.quality.SosQuality;
import org.n52.sos.ogc.om.values.BooleanValue;
import org.n52.sos.ogc.om.values.CategoryValue;
import org.n52.sos.ogc.om.values.CountValue;
import org.n52.sos.ogc.om.values.NilTemplateValue;
import org.n52.sos.ogc.om.values.QuantityValue;
import org.n52.sos.ogc.om.values.SweDataArrayValue;
import org.n52.sos.ogc.om.values.TextValue;
import org.n52.sos.ogc.om.values.UnknownValue;
import org.n52.sos.ogc.om.values.Value;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.SensorML;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.ogc.sos.SosProcedureDescriptionUnknowType;
import org.n52.sos.ogc.swe.SweAbstractDataComponent;
import org.n52.sos.ogc.swe.SweDataArray;
import org.n52.sos.ogc.swe.SweDataRecord;
import org.n52.sos.ogc.swe.simpleType.SweBoolean;
import org.n52.sos.ogc.swe.simpleType.SweCategory;
import org.n52.sos.ogc.swe.simpleType.SweCount;
import org.n52.sos.ogc.swe.simpleType.SweQuantity;
import org.n52.sos.ogc.swe.simpleType.SweText;
import org.n52.sos.ogc.swe.simpleType.SweTime;
import org.n52.sos.ogc.swe.simpleType.SweTimeRange;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.ServiceConfiguration;
import org.n52.sos.service.profile.Profile;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.DateTimeHelper;
import org.n52.sos.util.SosHelper;
import org.n52.sos.util.StringHelper;
import org.n52.sos.util.XmlHelper;

public class HibernateObservationUtilities {

    private static Configuration configuration;

    protected static Configuration getConfiguration() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    /**
     * Set the configuration for this Helper to decouple it from the
     * Configurator.
     * 
     * @param configuration
     *            the configuration
     */
    protected static void setConfiguration(final Configuration configuration) {
        HibernateObservationUtilities.configuration = configuration;
    }

    public static ContentCache getCache() {
        return getConfiguration().getCache();
    }

    public static Profile getActiveProfile() {
        return getConfiguration().getActiveProfile();
    }

    public static String getTokenSeparator() {
        return getConfiguration().getTokenSeparator();
    }

    public static String getTupleSeparator() {
        return getConfiguration().getTupleSeparator();
    }

    public static FeatureQueryHandler getFeatureQueryHandler() {
        return getConfiguration().getFeatureQueryHandler();
    }

    public static boolean isSupportsQuality() {
        return getConfiguration().isSupportsQuality();
    }

    /**
     * Create SOS internal observation from Observation objects
     * 
     * @param responseFormat
     * 
     * @param observations
     *            List of Observation objects
     * @param request
     *            the request
     * @param session
     *            Hibernate session
     * @return SOS internal observation
     * 
     * 
     * @throws OwsExceptionReport
     *             * If an error occurs
     */
    public static List<OmObservation> createSosObservationsFromObservations(
            final Collection<Observation> observations, final String version, final String resultModel, final Session session)
            throws OwsExceptionReport {
        final List<OmObservation> observationCollection = new ArrayList<OmObservation>(0);

        final Map<String, AbstractFeature> features = new HashMap<String, AbstractFeature>(0);
        final Map<String, AbstractPhenomenon> obsProps = new HashMap<String, AbstractPhenomenon>(0);
        final Map<String, SosProcedureDescription> procedures = new HashMap<String, SosProcedureDescription>(0);
        final Map<Integer, OmObservationConstellation> observationConstellations =
                new HashMap<Integer, OmObservationConstellation>(0);
        if (observations != null) {
            // now iterate over resultset and create Measurement for each row
            for (final Observation hObservation : observations) {
                // check remaining heap size and throw exception if minimum is
                // reached
                SosHelper.checkFreeMemory();
                final FeatureOfInterest hFeatureOfInterest = hObservation.getFeatureOfInterest();

                // TODO get full description
                final Procedure hProcedure = hObservation.getProcedure();
                final String procedureIdentifier = hProcedure.getIdentifier();
                SosProcedureDescription procedure;
                if (procedures.containsKey(procedureIdentifier)) {
                    procedure = procedures.get(procedureIdentifier);
                } else {
                    if (getConfiguration().getActiveProfile().isEncodeProcedureInObservation()) {
                        procedure =
                                new HibernateProcedureConverter().createSosProcedureDescription(
                                		hProcedure,
                                        procedureIdentifier,
                                        hProcedure.getProcedureDescriptionFormat().getProcedureDescriptionFormat(),
                                        version,
                                        session);
                    } else {
                        procedure =
                                new SosProcedureDescriptionUnknowType(procedureIdentifier, hProcedure
                                        .getProcedureDescriptionFormat().getProcedureDescriptionFormat(), null);
                    }
                    procedures.put(procedureIdentifier, procedure);
                }

                // feature of interest
                final String foiID = hFeatureOfInterest.getIdentifier();
                if (!features.containsKey(foiID)) {
                    final AbstractFeature featureByID =
                            getConfiguration().getFeatureQueryHandler().getFeatureByID(foiID, session, version, -1);
                    features.put(foiID, featureByID);
                }

                // phenomenon
                final ObservableProperty hObservableProperty = hObservation.getObservableProperty();
                final String phenID = hObservation.getObservableProperty().getIdentifier();
                final String description = hObservation.getObservableProperty().getDescription();
                if (!obsProps.containsKey(phenID)) {
                    obsProps.put(phenID, new OmObservableProperty(phenID, description, null, null));
                }

                // TODO: add offering ids to response if needed later.
                // String offeringID =
                // hObservationConstellation.getOffering().getIdentifier();
                // String mimeType = SosConstants.PARAMETER_NOT_SET;

                final Value<?> value = getValueFromObservation(hObservation);
                if (value != null) {
                    if (hObservation.getUnit() != null) {
                        value.setUnit(hObservation.getUnit().getUnit());
                    }
                    checkOrSetObservablePropertyUnit(obsProps.get(phenID), value.getUnit());
                    final OmObservationConstellation obsConst =
                            new OmObservationConstellation(procedure, obsProps.get(phenID), features.get(foiID));
                    /* get the offerings to find the templates */
                    if (obsConst.getOfferings() == null) {
                        final HashSet<String> offerings =
                                new HashSet<String>(getCache().getOfferingsForObservableProperty(
                                        obsConst.getObservableProperty().getIdentifier()));
                        offerings.retainAll(getCache().getOfferingsForProcedure(obsConst.getProcedure().getIdentifier()));
                        obsConst.setOfferings(offerings);
                    }
                    final int obsConstHash = obsConst.hashCode();
                    if (!observationConstellations.containsKey(obsConstHash)) {
                        if (StringHelper.isNotEmpty(resultModel)) {
                            obsConst.setObservationType(resultModel);
                        }
                        final ObservationConstellation hObservationConstellation =
                                getObservationConstellation(hProcedure, hObservableProperty, hObservation.getOfferings(),
                                        session);
                        if (hObservationConstellation != null) {
                            final String observationType = hObservationConstellation.getObservationType().getObservationType();
                            obsConst.setObservationType(observationType);
                        }
                        observationConstellations.put(obsConstHash, obsConst);
                    }
                    final OmObservation sosObservation =
                            createNewObservation(observationConstellations, hObservation, value, obsConstHash);
                    observationCollection.add(sosObservation);
                    session.evict(hObservation);
                    // TODO check for ScrollableResult vs setFetchSize/setMaxResult
                    // + setFirstResult
                }
            }
        }
        return observationCollection;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Collection<? extends OmObservation> createSosObservationFromObservationConstellation(
            final ObservationConstellation observationConstellation, final List<String> featureOfInterestIdentifiers,
            final String version, final Session session) throws OwsExceptionReport {
        final List<OmObservation> observations = new ArrayList<OmObservation>(0);
        if (observationConstellation != null && featureOfInterestIdentifiers != null) {
            final String procID = observationConstellation.getProcedure().getIdentifier();
            final SensorML procedure = new SensorML();
            procedure.setIdentifier(procID);
            // phenomenon
            final String phenID = observationConstellation.getObservableProperty().getIdentifier();
            final String description = observationConstellation.getObservableProperty().getDescription();
            final OmObservableProperty obsProp = new OmObservableProperty(phenID, description, null, null);

            for (final String featureIdentifier : featureOfInterestIdentifiers) {
                final AbstractFeature feature =
                        getFeatureQueryHandler().getFeatureByID(featureIdentifier, session, version, -1);

                final OmObservationConstellation obsConst =
                        new OmObservationConstellation(procedure, obsProp, null, feature, null);
                /* get the offerings to find the templates */
                if (obsConst.getOfferings() == null) {
                    final Set<String> offerings =
                            new HashSet<String>(getCache().getOfferingsForProcedure(
                                    obsConst.getProcedure().getIdentifier()));
                    offerings.retainAll(new HashSet<String>(getCache().getOfferingsForProcedure(
                            obsConst.getProcedure().getIdentifier())));
                    obsConst.setOfferings(offerings);
                }
                final OmObservation sosObservation = new OmObservation();
                sosObservation.setNoDataValue(getActiveProfile().getResponseNoDataPlaceholder());
                sosObservation.setTokenSeparator(getTokenSeparator());
                sosObservation.setTupleSeparator(getTupleSeparator());
                sosObservation.setObservationConstellation(obsConst);
                final NilTemplateValue value = new NilTemplateValue();
                value.setUnit(obsProp.getUnit());
                sosObservation.setValue(new SingleObservationValue(new TimeInstant(), value,
                        new ArrayList<SosQuality>(0)));
                observations.add(sosObservation);
            }
        }
        return observations;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static OmObservation createNewObservation(
            final Map<Integer, OmObservationConstellation> observationConstellations, final Observation hObservation,
            final Value<?> value, final int obsConstHash) {
        final OmObservation sosObservation = new OmObservation();
        sosObservation.setObservationID(Long.toString(hObservation.getObservationId()));
        if (hObservation.isSetIdentifier()
                && !hObservation.getIdentifier().startsWith(SosConstants.GENERATED_IDENTIFIER_PREFIX)) {
            final CodeWithAuthority identifier = new CodeWithAuthority(hObservation.getIdentifier());
            if (hObservation.isSetCodespace()) {
                identifier.setCodeSpace(hObservation.getCodespace().getCodespace());
            }
            sosObservation.setIdentifier(identifier);
        }
        sosObservation.setNoDataValue(getActiveProfile().getResponseNoDataPlaceholder());
        sosObservation.setTokenSeparator(getTokenSeparator());
        sosObservation.setTupleSeparator(getTupleSeparator());
        sosObservation.setObservationConstellation(observationConstellations.get(obsConstHash));
        sosObservation.setResultTime(new TimeInstant(new DateTime(hObservation.getResultTime(), DateTimeZone.UTC)));
        sosObservation.setValue(new SingleObservationValue(getPhenomenonTime(hObservation), value));
        return sosObservation;
    }

    private static Time getPhenomenonTime(final Observation hObservation) {
        // create time element
        final DateTime phenStartTime = new DateTime(hObservation.getPhenomenonTimeStart(), DateTimeZone.UTC);
        DateTime phenEndTime;
        if (hObservation.getPhenomenonTimeEnd() != null) {
            phenEndTime = new DateTime(hObservation.getPhenomenonTimeEnd(), DateTimeZone.UTC);
        } else {
            phenEndTime = phenStartTime;
        }
        Time phenomenonTime;
        if (phenStartTime.equals(phenEndTime)) {
            phenomenonTime = new TimeInstant(phenStartTime, "");
        } else {
            phenomenonTime = new TimePeriod(phenStartTime, phenEndTime);
        }
        return phenomenonTime;
    }

    private static void checkOrSetObservablePropertyUnit(final AbstractPhenomenon abstractSosPhenomenon,
            final String unit) {
        if (abstractSosPhenomenon instanceof OmObservableProperty) {
            final OmObservableProperty obsProp = (OmObservableProperty) abstractSosPhenomenon;
            if (obsProp.getUnit() == null && unit != null) {
                obsProp.setUnit(unit);
            }
        }
    }

    /**
     * Get observation value from all value tables for an Observation object
     * 
     * @param hObservation
     *            Observation object
     * @return Observation value
     * @throws OwsExceptionReport 
     * @throws CodedException 
     */
    private static Value<?> getValueFromObservation(final Observation hObservation) throws CodedException, OwsExceptionReport {
        if (hObservation instanceof NumericObservation) {
            return new QuantityValue(((NumericObservation) hObservation).getValue());
        } else if (hObservation instanceof BooleanObservation) {
            return new org.n52.sos.ogc.om.values.BooleanValue(Boolean.valueOf(((BooleanObservation) hObservation)
                    .getValue()));
        } else if (hObservation instanceof CategoryObservation) {
            return new org.n52.sos.ogc.om.values.CategoryValue(((CategoryObservation) hObservation).getValue());
        } else if (hObservation instanceof CountObservation) {
            return new org.n52.sos.ogc.om.values.CountValue(Integer.valueOf(((CountObservation) hObservation)
                    .getValue()));
        } else if (hObservation instanceof TextObservation) {
            return new org.n52.sos.ogc.om.values.TextValue(((TextObservation) hObservation).getValue().toString());
        } else if (hObservation instanceof GeometryObservation) {
            return new org.n52.sos.ogc.om.values.GeometryValue(((GeometryObservation) hObservation).getValue());
        } else if (hObservation instanceof BlobObservation) {
            return new UnknownValue(((BlobObservation) hObservation).getValue());
        } else if (hObservation instanceof SweDataArrayObservation) {
            SweDataArrayValue sweDataArrayValue = new SweDataArrayValue();
            sweDataArrayValue.setValue((SweDataArray)CodingHelper.decodeXmlElement(XmlHelper.parseXmlString(((SweDataArrayObservation) hObservation).getValue())));
            return sweDataArrayValue;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static List<OmObservation> unfoldObservation(final OmObservation multiObservation)
            throws OwsExceptionReport {
        if (multiObservation.getValue() instanceof SingleObservationValue) {
            return Collections.singletonList(multiObservation);
        } else {

            final SweDataArrayValue arrayValue =
                    ((SweDataArrayValue) ((MultiObservationValues) multiObservation.getValue()).getValue());
            final List<List<String>> values = arrayValue.getValue().getValues();
            final List<OmObservation> observationCollection = new ArrayList<OmObservation>(values.size());
            SweDataRecord elementType = null;
            if (arrayValue.getValue().getElementType() != null
                    && arrayValue.getValue().getElementType() instanceof SweDataRecord) {
                elementType = (SweDataRecord) arrayValue.getValue().getElementType();
            } else {
                throw new NoApplicableCodeException().withMessage("sweElementType type \"%s\" not supported",
                        elementType != null ? elementType.getClass().getName() : "null");
            }

            for (final List<String> block : values) {
                int tokenIndex = 0;
                Time phenomenonTime = null;
                final List<Value<?>> observedValues = new LinkedList<Value<?>>();
                // map to store the observed properties
                final Map<Value<?>, String> definitionsForObservedValues = new HashMap<Value<?>, String>();
                Value<?> observedValue = null;
                for (final String token : block) {
                    // get values from block via definition in
                    // SosSweDataArray#getElementType
                    final SweAbstractDataComponent fieldForToken =
                            elementType.getFields().get(tokenIndex).getElement();
                    /*
                     * get phenomenon time
                     */
                    if (fieldForToken instanceof SweTime) {
                        try {
                            phenomenonTime = new TimeInstant(DateTimeHelper.parseIsoString2DateTime(token));
                        } catch (final OwsExceptionReport e) {
                            throw e;
                        } catch (final Exception e) {
                            /*
                             * FIXME what is the valid exception code if the
                             * result is not correct?
                             */
                            throw new NoApplicableCodeException().causedBy(e).withMessage(
                                    "Error while parse time String to DateTime!");
                        }
                    } else if (fieldForToken instanceof SweTimeRange) {
                        try {
                            final String[] subTokens = token.split("/");
                            phenomenonTime =
                                    new TimePeriod(DateTimeHelper.parseIsoString2DateTime(subTokens[0]),
                                            DateTimeHelper.parseIsoString2DateTime(subTokens[1]));
                        } catch (final OwsExceptionReport e) {
                            throw e;
                        } catch (final Exception e) {
                            /*
                             * FIXME what is the valid exception code if the
                             * result is not correct?
                             */
                            throw new NoApplicableCodeException().causedBy(e).withMessage(
                                    "Error while parse time String to DateTime!");
                        }
                    }
                    /*
                     * observation values
                     */
                    else if (fieldForToken instanceof SweQuantity) {
                        observedValue = new QuantityValue(new BigDecimal(token));
                        observedValue.setUnit(((SweQuantity) fieldForToken).getUom());
                    } else if (fieldForToken instanceof SweBoolean) {
                        observedValue = new BooleanValue(Boolean.parseBoolean(token));
                    } else if (fieldForToken instanceof SweText) {
                        observedValue = new TextValue(token);
                    } else if (fieldForToken instanceof SweCategory) {
                        observedValue = new CategoryValue(token);
                        observedValue.setUnit(((SweCategory) fieldForToken).getCodeSpace());
                    } else if (fieldForToken instanceof SweCount) {
                        observedValue = new CountValue(Integer.parseInt(token));
                    } else {
                        throw new NoApplicableCodeException().withMessage("sweField type '%s' not supported",
                                fieldForToken != null ? fieldForToken.getClass().getName() : "null");
                    }
                    if (observedValue != null) {
                        definitionsForObservedValues.put(observedValue, fieldForToken.getDefinition());
                        observedValues.add(observedValue);
                        observedValue = null;
                    }
                    tokenIndex++;
                }
                for (final Value<?> iValue : observedValues) {
                    final OmObservation newObservation =
                            createSingleValueObservation(multiObservation, phenomenonTime, iValue);
                    observationCollection.add(newObservation);
                }
            }
            return observationCollection;
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static OmObservation createSingleValueObservation(final OmObservation multiObservation,
            final Time phenomenonTime, final Value<?> iValue) {
        final ObservationValue<?> value = new SingleObservationValue(phenomenonTime, iValue);
        final OmObservation newObservation = new OmObservation();
        newObservation.setNoDataValue(multiObservation.getNoDataValue());
        /*
         * TODO create new ObservationConstellation only with the specified
         * observed property and observation type
         */
        final OmObservationConstellation obsConst = multiObservation.getObservationConstellation();
        /*
         * createObservationConstellationForSubObservation ( multiObservation .
         * getObservationConstellation ( ) , iValue ,
         * definitionsForObservedValues . get ( iValue ) )
         */
        newObservation.setObservationConstellation(obsConst);
        newObservation.setValidTime(multiObservation.getValidTime());
        newObservation.setResultTime(multiObservation.getResultTime());
        newObservation.setTokenSeparator(multiObservation.getTokenSeparator());
        newObservation.setTupleSeparator(multiObservation.getTupleSeparator());
        newObservation.setResultType(multiObservation.getResultType());
        newObservation.setValue(value);
        return newObservation;
    }

    public static ObservationConstellation getObservationConstellation(final Procedure procedure,
            final ObservableProperty observableProperty, final Collection<Offering> offerings, final Session session) {
        @SuppressWarnings("unchecked")
        final List<ObservationConstellation> observationConstellations =
                session.createCriteria(ObservationConstellation.class)
                        .add(Restrictions.eq(ObservationConstellation.DELETED, false))
                        .add(Restrictions.eq(ObservationConstellation.PROCEDURE, procedure))
                        .add(Restrictions.in(ObservationConstellation.OFFERING, offerings))
                        .add(Restrictions.eq(ObservationConstellation.OBSERVABLE_PROPERTY, observableProperty)).list();
        final Iterator<ObservationConstellation> iterator = observationConstellations.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    private HibernateObservationUtilities() {
    }

    /**
     * Class to make this Helper more testable. Test cases may overwrite methods
     * to decouple this class from the Configurator.
     */
    protected static class Configuration {

        /**
         * @see ServiceConfiguration#getTupleSeparator()
         */
        protected String getTupleSeparator() {
            return ServiceConfiguration.getInstance().getTupleSeparator();
        }

        /**
         * @see ServiceConfiguration#getTokenSeparator()
         */
        protected String getTokenSeparator() {
            return ServiceConfiguration.getInstance().getTokenSeparator();
        }

        /**
         * @see Configurator#getCapabilitiesCacheController()
         */
        protected ContentCache getCache() {
            return Configurator.getInstance().getCache();
        }

        /**
         * @see Configurator#getActiveProfile()
         */
        protected Profile getActiveProfile() {
            return Configurator.getInstance().getProfileHandler().getActiveProfile();
        }

        /**
         * @see Configurator#getFeatureQueryHandler()
         */
        protected FeatureQueryHandler getFeatureQueryHandler() {
            return Configurator.getInstance().getFeatureQueryHandler();
        }

        /**
         * @see ServiceConfiguration#isSupportsQuality()
         */
        protected boolean isSupportsQuality() {
            return ServiceConfiguration.getInstance().isSupportsQuality();
        }
    }
}
