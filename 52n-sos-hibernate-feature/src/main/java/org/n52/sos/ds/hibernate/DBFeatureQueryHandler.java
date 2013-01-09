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
package org.n52.sos.ds.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.spatial.criterion.SpatialProjections;
import org.n52.sos.ds.IFeatureQueryHandler;
import org.n52.sos.ds.hibernate.entities.FeatureOfInterest;
import org.n52.sos.ds.hibernate.util.HibernateConstants;
import org.n52.sos.ds.hibernate.util.HibernateCriteriaQueryUtilities;
import org.n52.sos.ds.hibernate.util.HibernateFeatureCriteriaQueryUtilities;
import org.n52.sos.ds.hibernate.util.HibernateFeatureCriteriaTransactionalUtilities;
import org.n52.sos.ogc.filter.SpatialFilter;
import org.n52.sos.ogc.om.features.SosAbstractFeature;
import org.n52.sos.ogc.om.features.samplingFeatures.SosSamplingFeature;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.util.SosHelper;
import org.n52.sos.util.Util4Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Feature handler class for features stored in the database
 * 
 */
public class DBFeatureQueryHandler implements IFeatureQueryHandler {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DBFeatureQueryHandler.class);

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.sos.ds.IFeatureQueryHandler#getFeatureByID(java.lang.String)
     */
    @Override
    public SosAbstractFeature getFeatureByID(String featureID, Object connection, String version)
            throws OwsExceptionReport {
        Session session = getSessionFromConnection(connection);
        try {
            Criteria criteria = session.createCriteria(FeatureOfInterest.class);
            criteria.add(Restrictions.eq("identifier", featureID));
            return createSosAbstractFeatureFromResult((FeatureOfInterest) criteria.uniqueResult(), version);
        } catch (HibernateException he) {
            String exceptionText = "An error occurs while querying feature data for a featureOfInterest identifier!";
            LOGGER.error(exceptionText, he);
            throw Util4Exceptions.createNoApplicableCodeException(he, exceptionText);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.sos.ds.IFeatureQueryHandler#getFeatureIDs(java.util.List,
     * org.n52.sos.ogc.filter.SpatialFilter)
     */
    @Override
    public Collection<String> getFeatureIDs(SpatialFilter filter, Object connection) throws OwsExceptionReport {
        Session session = getSessionFromConnection(connection);
        HibernateQueryObject queryObject = new HibernateQueryObject();
        try {
            if (filter != null) {
                String propertyName = HibernateConstants.PARAMETER_GEOMETRY;
                queryObject.addCriterion(HibernateCriteriaQueryUtilities.getCriterionForSpatialFilter(propertyName,
                        filter));
            }
            return HibernateCriteriaQueryUtilities.getFeatureOfInterestIdentifier(queryObject, session);
        } catch (HibernateException he) {
            String exceptionText =
                    "An error occurs while querying feature identifiers for a featureOfInterest identifier!";
            LOGGER.error(exceptionText, he);
            throw Util4Exceptions.createNoApplicableCodeException(he, exceptionText);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.sos.ds.IFeatureQueryHandler#getFeatures(java.util.List,
     * org.n52.sos.ogc.filter.SpatialFilter)
     */
    @Override
    public Map<String, SosAbstractFeature> getFeatures(List<String> featureIDs, List<SpatialFilter> spatialFilters,
            Object connection, String version) throws OwsExceptionReport {
        Session session = getSessionFromConnection(connection);
        HibernateQueryObject queryObject = new HibernateQueryObject();
        try {
            if (featureIDs != null && !featureIDs.isEmpty()) {
                queryObject.addCriterion(HibernateCriteriaQueryUtilities.getDisjunctionCriterionForStringList(
                        HibernateConstants.PARAMETER_IDENTIFIER, featureIDs));
            }
            if (spatialFilters != null && !spatialFilters.isEmpty()) {
                // String foiAlias = HibernateCriteriaQueryUtilities
                // .addFeatureOfInterestAliasToMap(aliases, null);
                // String propertyName = foiAlias + "."
                // + HibernateConstants.PARAMETER_GEOMETRY;
                String propertyName = HibernateConstants.PARAMETER_GEOMETRY;
                Disjunction disjunction = Restrictions.disjunction();
                for (SpatialFilter filter : spatialFilters) {
                    disjunction
                            .add(HibernateCriteriaQueryUtilities.getCriterionForSpatialFilter(propertyName, filter));
                }
                queryObject.addCriterion(disjunction);
            }
            if (queryObject.isSetCriterions()) {
                return createSosAbstractFeaturesFromResult(
                        HibernateFeatureCriteriaQueryUtilities.getFeatureOfInterests(queryObject, session), version);
            } else {
                return new HashMap<String, SosAbstractFeature>(0);
            }

        } catch (HibernateException he) {
            String exceptionText = "Error while querying features from data source!";
            LOGGER.error(exceptionText, he);
            throw Util4Exceptions.createNoApplicableCodeException(he, exceptionText);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.n52.sos.ds.IFeatureQueryHandler#getEnvelopeforFeatureIDs(java.util
     * .List)
     */
    @Override
    public Envelope getEnvelopeForFeatureIDs(List<String> featureIDs, Object connection) throws OwsExceptionReport {
        Session session = getSessionFromConnection(connection);
        if (featureIDs != null && !featureIDs.isEmpty()) {
            try {
                Criteria criteria = session.createCriteria(FeatureOfInterest.class);
                criteria.add(Restrictions.in(HibernateConstants.PARAMETER_IDENTIFIER, featureIDs));
                criteria.setProjection(SpatialProjections.extent("geom"));
                Geometry geom = (Geometry) criteria.uniqueResult();
                if (geom != null) {
                    return geom.getEnvelopeInternal();
                }
            } catch (HibernateException he) {
                String exceptionText = "";
                LOGGER.error(exceptionText, he);
                throw Util4Exceptions.createNoApplicableCodeException(he, exceptionText);
            }
        }
        return null;
    }

    @Override
    public String insertFeature(SosSamplingFeature samplingFeature, Object connection) throws OwsExceptionReport {
        if (samplingFeature.getUrl() != null && !samplingFeature.getUrl().isEmpty()) {
            return samplingFeature.getUrl();
        } else {
            Session session = getSessionFromConnection(connection);
            String featureIdentifier = null;
            if (samplingFeature.getIdentifier() != null && !samplingFeature.getIdentifier().isEmpty()) {
                featureIdentifier = samplingFeature.getIdentifier();
            } else {
                featureIdentifier = "generated_" + SosHelper.generateID(samplingFeature.getXmlDescription());
                samplingFeature.setIdentifier(featureIdentifier);
            }
            HibernateFeatureCriteriaTransactionalUtilities.insertFeatureOfInterest(samplingFeature, session);
            return featureIdentifier;
        }
    }

    /**
     * Creates a map with FOI identifier and SOS feature
     * 
     * @param features
     *            FeatureOfInterest objects
     * @param version
     *            SOS version
     * @return Map with FOI identifier and SOS feature
     * @throws OwsExceptionReport
     *             If feature type is not supported
     */
    private Map<String, SosAbstractFeature> createSosAbstractFeaturesFromResult(List<FeatureOfInterest> features,
            String version) throws OwsExceptionReport {
        Map<String, SosAbstractFeature> sosAbstractFois = new HashMap<String, SosAbstractFeature>();
        for (FeatureOfInterest feature : features) {
            sosAbstractFois.put(feature.getIdentifier(), createSosAbstractFeatureFromResult(feature, version));
        }
        // TODO if sampledFeatures are also in sosAbstractFois, reference them.
        return sosAbstractFois;
    }

    /**
     * Creates a SOS feature from the FeatureOfInterest object
     * 
     * @param feature
     *            FeatureOfInterest object
     * @param version
     *            SOS version
     * @return SOS feature
     * @throws OwsExceptionReport
     *             If feature type is not supported
     */
    private SosAbstractFeature createSosAbstractFeatureFromResult(FeatureOfInterest feature, String version)
            throws OwsExceptionReport {
        String checkedFoiID = null;
        if (SosHelper.checkFeatureOfInterestIdentifierForSosV2(feature.getIdentifier(), version)) {
            checkedFoiID = feature.getIdentifier();
        }
        SosSamplingFeature sampFeat = new SosSamplingFeature(checkedFoiID);
        if (feature.getName() != null && !feature.getName().isEmpty()) {
            sampFeat.setName(SosHelper.createListFromCSV(feature.getName()));
        }
        sampFeat.setDescription(null);
        sampFeat.setGeometry(feature.getGeom());
        if (feature.getGeom() != null) {
            sampFeat.setEpsgCode(feature.getGeom().getSRID());
        }
        sampFeat.setFeatureType(feature.getFeatureOfInterestType().getFeatureOfInterestType());
        sampFeat.setUrl(feature.getUrl());
        sampFeat.setXmlDescription(feature.getDescriptionXml());
        Set<FeatureOfInterest> parentFeatures = feature.getFeatureOfInterestsForParentFeatureId();
        if (parentFeatures != null && !parentFeatures.isEmpty()) {
            List<SosAbstractFeature> sampledFeatures = new ArrayList<SosAbstractFeature>(parentFeatures.size());
            for (FeatureOfInterest parentFeature : parentFeatures) {
                sampledFeatures.add(createSosAbstractFeatureFromResult(parentFeature, version));
            }
            sampFeat.setSampledFeatures(sampledFeatures);
        }
        return sampFeat;
    }

    /**
     * Checks if connection is a Hibernate Session and casts the connection
     * 
     * @param connection
     *            connection
     * @return Hibernate Session
     * @throws OwsExceptionReport
     *             If connection is not a Hibentat Sessrion
     */
    private Session getSessionFromConnection(Object connection) throws OwsExceptionReport {
        if (connection instanceof Session) {
            return (Session) connection;
        } else {
            String exceptionText = "The connection is not a Hibernate Session!";
            LOGGER.error(exceptionText);
            throw Util4Exceptions.createNoApplicableCodeException(null, exceptionText);
        }
    }
}
