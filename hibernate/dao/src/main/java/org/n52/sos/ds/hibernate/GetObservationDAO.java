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
package org.n52.sos.ds.hibernate;

import static org.n52.sos.ds.hibernate.util.HibernateObservationUtilities.createSosObservationFromObservationConstellation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.n52.sos.ds.AbstractGetObservationDAO;
import org.n52.sos.ds.hibernate.dao.FeatureOfInterestDAO;
import org.n52.sos.ds.hibernate.dao.ObservationDAO;
import org.n52.sos.ds.hibernate.entities.FeatureOfInterest;
import org.n52.sos.ds.hibernate.entities.ObservableProperty;
import org.n52.sos.ds.hibernate.entities.Observation;
import org.n52.sos.ds.hibernate.entities.ObservationConstellation;
import org.n52.sos.ds.hibernate.entities.Offering;
import org.n52.sos.ds.hibernate.entities.Procedure;
import org.n52.sos.ds.hibernate.util.HibernateObservationUtilities;
import org.n52.sos.ds.hibernate.util.QueryHelper;
import org.n52.sos.ds.hibernate.util.TemporalRestrictions;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.exception.ows.concrete.MissingObservedPropertyParameterException;
import org.n52.sos.ogc.filter.TemporalFilter;
import org.n52.sos.ogc.om.OmObservation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos1Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosConstants.IndeterminateTime;
import org.n52.sos.request.GetObservationRequest;
import org.n52.sos.response.GetObservationResponse;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.HTTPConstants.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the interface IGetObservationDAO
 * 
 */
public class GetObservationDAO extends AbstractGetObservationDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetObservationDAO.class);

    private final HibernateSessionHolder sessionHolder = new HibernateSessionHolder();

    public GetObservationDAO() {
        super(SosConstants.SOS);
    }

    @Override
    public GetObservationResponse getObservation(final GetObservationRequest sosRequest) throws OwsExceptionReport {
        Session session = null;
        try {
            session = sessionHolder.getSession();
            if (sosRequest.getVersion().equals(Sos1Constants.SERVICEVERSION)
                    && sosRequest.getObservedProperties().isEmpty()) {
                throw new MissingObservedPropertyParameterException();
            } else {
                final GetObservationResponse sosResponse = new GetObservationResponse();
                sosResponse.setService(sosRequest.getService());
                sosResponse.setVersion(sosRequest.getVersion());
                sosResponse.setResponseFormat(sosRequest.getResponseFormat());
                if (sosRequest.isSetResultModel()) {
                    sosResponse.setResultModel(sosRequest.getResultModel());
                }
                if (getConfigurator().getProfileHandler().getActiveProfile().isShowMetadataOfEmptyObservations()) {
                    // TODO Hydro-Profile adds result observation metadata to
                    // response
                    sosResponse.setObservationCollection(queryObservationHydro(sosRequest, session));
                } else {
                    sosResponse.setObservationCollection(queryObservation(sosRequest, session));
                }
                return sosResponse;
            }
        } catch (final HibernateException he) {
            throw new NoApplicableCodeException().causedBy(he).withMessage("Error while querying observation data!")
                    .setStatus(StatusCode.INTERNAL_SERVER_ERROR);
        } finally {
            sessionHolder.returnSession(session);
        }
    }

    @SuppressWarnings("unchecked")
    protected List<OmObservation> queryObservation(final GetObservationRequest request, final Session session)
            throws OwsExceptionReport {
        // TODO Threadable !?!
        // TODO How to ensure no duplicated observations ?!
        // TODO How to ensure that anti subsetting observation are also included
        // ?!

        final long start = System.currentTimeMillis();
        final Set<String> features = getFeatures(request, session);
        if (features != null && features.isEmpty()) {
            return null;
        }

        List<Observation> observations = new LinkedList<Observation>();
        final Criterion filterCriterion = getTemporalFilterCriterion(request);

        if (request.hasTemporalFilters()) {
            final List<IndeterminateTime> flFilters = request.getFirstLatestTemporalFilter();
            if (!flFilters.isEmpty()) {
                for (final IndeterminateTime fl : flFilters) {
                    observations.addAll(createTemporalFilterLessCriteria(session, request, features)
                            .addOrder(getOrder(fl)).setMaxResults(1).list());
                }
            } else if (filterCriterion != null) {
                observations =
                        createTemporalFilterLessCriteria(session, request, features).add(filterCriterion).list();
            }
        } else {
            observations = createTemporalFilterLessCriteria(session, request, features).list();

        }
        LOGGER.debug("Time to query observations needed {} ms!", (System.currentTimeMillis() - start));
        return toSosObservation(observations, request.getVersion(), request.getResultModel(), session);
    }

    /**
     * Query observations from database depending on requested filters
     * 
     * @param request
     *            GetObservation request
     * @param session
     *            Hibernate session
     * @return List of Observation objects
     * 
     * 
     * @throws OwsExceptionReport
     *             * If an error occurs.
     */
    @SuppressWarnings("unchecked")
    protected List<OmObservation> queryObservationHydro(final GetObservationRequest request, final Session session)
            throws OwsExceptionReport {
        // TODO Threadable !?!
        // TODO How to ensure no duplicated observations ?!
        // TODO How to ensure that anti subsetting observation are also included
        // ?!

        final long start = System.currentTimeMillis();
        final Set<String> features = getFeatures(request, session);
        if (features != null && features.isEmpty()) {
            return null;
        }
        // temporal filters
        final List<IndeterminateTime> itFilters = request.getFirstLatestTemporalFilter();
        final Criterion filterCriterion = getTemporalFilterCriterion(request);

        final List<OmObservation> result = new LinkedList<OmObservation>();
        final Set<Observation> allObservations = new HashSet<Observation>(0);
        for (final ObservationConstellation oc : getObservationConstellations(session, request)) {
            final Set<Observation> observations = new HashSet<Observation>(0);
            if (request.hasTemporalFilters()) {
                if (itFilters != null && !itFilters.isEmpty()) {
                    for (final IndeterminateTime indetTimeFiler : itFilters) {
                        observations.addAll(createObservationCriteria(session, oc).addOrder(getOrder(indetTimeFiler))
                                .setMaxResults(1).list());
                    }
                } else if (filterCriterion != null) {
                    observations.addAll(createObservationCriteria(session, oc).add(filterCriterion).list());
                }
            } else {
                observations.addAll(createObservationCriteria(session, oc).list());
            }
            if (observations.isEmpty()) {
                // if no observations were found, create a "result" observation
                final List<String> featureIds = getAndCheckFeatureOfInterest(oc, features, session);
                result.addAll(createSosObservationFromObservationConstellation(oc, featureIds, request.getVersion(),
                        session));
            } else {
                allObservations.addAll(observations);
            }
        }

        LOGGER.debug("Time to query observations needs {} ms!", (System.currentTimeMillis() - start));
        result.addAll(toSosObservation(allObservations, request.getVersion(), request.getResultModel(), session));
        return result;
    }

    private List<String> getAndCheckFeatureOfInterest(final ObservationConstellation observationConstellation,
            final Set<String> featureIdentifier, final Session session) {
        final List<String> featuresForConstellation =
                new FeatureOfInterestDAO().getFeatureOfInterestIdentifiersForObservationConstellation(
                        observationConstellation, session);
        if (featureIdentifier == null) {
            return featuresForConstellation;
        } else {
            return CollectionHelper.conjunctCollections(featuresForConstellation, featureIdentifier);
        }
    }

    protected List<OmObservation> toSosObservation(final Collection<Observation> observations, final String version,
            final String resultModel, final Session session) throws OwsExceptionReport {
        if (!observations.isEmpty()) {
            final long startProcess = System.currentTimeMillis();
            final List<OmObservation> sosObservations =
                    HibernateObservationUtilities.createSosObservationsFromObservations(new HashSet<Observation>(
                            observations), version, resultModel, session);
            LOGGER.debug("Time to process observations needs {} ms!", (System.currentTimeMillis() - startProcess));
            return sosObservations;
        } else {
            return Collections.emptyList();
        }
    }

    protected Set<String> getFeatures(final GetObservationRequest request, final Session session)
            throws OwsExceptionReport {
        return QueryHelper.getFeatureIdentifier(request.getSpatialFilter(), request.getFeatureIdentifiers(), session);
    }

    protected Criteria createTemporalFilterLessCriteria(final Session session, final GetObservationRequest request,
            final Set<String> features) throws HibernateException {

        final Criteria c =
                new ObservationDAO().getObservationClassCriteriaForResultModel(request.getResultModel(), session);
        if (request.isSetOffering()) {
            c.createCriteria(Observation.OFFERINGS).add(Restrictions.in(Offering.IDENTIFIER, request.getOfferings()));
        }
        if (request.isSetObservableProperty()) {
            c.createCriteria(Observation.OBSERVABLE_PROPERTY).add(
                    Restrictions.in(ObservableProperty.IDENTIFIER, request.getObservedProperties()));
        }
        if (request.isSetProcedure()) {
            c.createCriteria(Observation.PROCEDURE)
                    .add(Restrictions.in(Procedure.IDENTIFIER, request.getProcedures()));
        }
        if (features != null) {
            c.createCriteria(Observation.FEATURE_OF_INTEREST).add(
                    Restrictions.in(FeatureOfInterest.IDENTIFIER, features));
        }
        return c;
    }

    @SuppressWarnings("unchecked")
    protected List<ObservationConstellation> getObservationConstellations(final Session session,
            final GetObservationRequest request) throws HibernateException {
        final Criteria c =
                session.createCriteria(ObservationConstellation.class)
                        .add(Restrictions.eq(ObservationConstellation.DELETED, false))
                        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (request.isSetOffering()) {
            c.createCriteria(ObservationConstellation.OFFERING).add(
                    Restrictions.in(Offering.IDENTIFIER, request.getOfferings()));
        }
        if (request.isSetObservableProperty()) {
            c.createCriteria(ObservationConstellation.OBSERVABLE_PROPERTY).add(
                    Restrictions.in(ObservableProperty.IDENTIFIER, request.getObservedProperties()));
        }
        if (request.isSetProcedure()) {
            c.createCriteria(ObservationConstellation.PROCEDURE).add(
                    Restrictions.in(Procedure.IDENTIFIER, request.getProcedures()));
        }
        return c.add(Restrictions.isNotNull(ObservationConstellation.OBSERVATION_TYPE)).list();
    }

    protected Criteria createObservationCriteria(final Session session, final ObservationConstellation oc)
            throws HibernateException {
        final Criteria c =
                session.createCriteria(Observation.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                        .add(Restrictions.eq(Observation.DELETED, false))
                        .add(Restrictions.eq(Observation.OBSERVABLE_PROPERTY, oc.getObservableProperty()))
                        .add(Restrictions.eq(Observation.PROCEDURE, oc.getProcedure()));
        c.createCriteria(Observation.OFFERINGS).add(Restrictions.eq(Offering.ID, oc.getOffering().getOfferingId()));
        return c;
    }

    protected Criterion getTemporalFilterCriterion(final GetObservationRequest request) throws OwsExceptionReport {
        final List<TemporalFilter> filters = request.getNotFirstLatestTemporalFilter();
        if (request.hasTemporalFilters() && CollectionHelper.isNotEmpty(filters)) {
            return TemporalRestrictions.filter(filters);
        } else {
            return null;
        }
    }

    protected Order getOrder(final IndeterminateTime indetTime) {
        if (indetTime.equals(IndeterminateTime.first)) {
            return Order.asc(Observation.PHENOMENON_TIME_START);
        } else if (indetTime.equals(IndeterminateTime.latest)) {
            return Order.desc(Observation.PHENOMENON_TIME_END);
        }
        return null;
    }
}
