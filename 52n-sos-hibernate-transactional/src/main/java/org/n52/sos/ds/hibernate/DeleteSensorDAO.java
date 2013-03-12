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


import static org.n52.sos.ds.hibernate.util.HibernateCriteriaTransactionalUtilities.setValidProcedureDescriptionEndTime;
import static org.n52.sos.util.Util4Exceptions.createNoApplicableCodeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.n52.sos.ds.AbstractDeleteSensorDAO;
import org.n52.sos.ds.hibernate.entities.Observation;
import org.n52.sos.ds.hibernate.entities.ObservationConstellationOfferingObservationType;
import org.n52.sos.ds.hibernate.entities.Procedure;
import org.n52.sos.ds.hibernate.util.HibernateCriteriaQueryUtilities;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.DeleteSensorRequest;
import org.n52.sos.response.DeleteSensorResponse;
import org.n52.sos.util.Util4Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteSensorDAO extends AbstractDeleteSensorDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteSensorDAO.class);

   private HibernateSessionHolder sessionHolder = new HibernateSessionHolder();

    @Override
    public synchronized DeleteSensorResponse deleteSensor(DeleteSensorRequest request) throws OwsExceptionReport {
        DeleteSensorResponse response = new DeleteSensorResponse();
        response.setService(request.getService());
        response.setVersion(request.getVersion());
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionHolder.getSession();
            transaction = session.beginTransaction();
            setDeleteSensorFlag(request.getProcedureIdentifier(), true, session);
            setValidProcedureDescriptionEndTime(request.getProcedureIdentifier(), session);
            // FIXME set all obs to deleted
            transaction.commit();
            response.setDeletedProcedure(request.getProcedureIdentifier());
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
            String exceptionText = "Error while updateing deleted sensor flag data!";
            LOGGER.error(exceptionText, he);
            throw createNoApplicableCodeException(he, exceptionText);
        } finally {
            sessionHolder.returnSession(session);
        }
        return response;
    }
    
    private void setDeleteSensorFlag(String identifier, boolean deleteFlag, Session session)
            throws OwsExceptionReport {
        Procedure procedure = HibernateCriteriaQueryUtilities.getProcedureForIdentifier(identifier, session);
        if (procedure != null) {
            procedure.setDeleted(deleteFlag);
            session.saveOrUpdate(procedure);
            session.flush();
            setObservationConstellationOfferingObservationTypeAsDeletedForProcedure(identifier, session);
            setObservationsAsDeletedForProcedure(identifier, session);
        } else {
            String exceptionText = "The requested identifier is not contained in database";
            throw Util4Exceptions.createNoApplicableCodeException(null, exceptionText);
        }
    }
    
    private void setObservationConstellationOfferingObservationTypeAsDeletedForProcedure(
            String procedureIdentifier, Session session) {
        HibernateQueryObject queryObject = new HibernateQueryObject();
        Map<String, String> aliases = new HashMap<String, String>(0);
        String obsConstAlias = HibernateCriteriaQueryUtilities.addObservationConstallationAliasToMap(aliases, null);
        String procAlias = HibernateCriteriaQueryUtilities.addProcedureAliasToMap(aliases, obsConstAlias);
        queryObject.addCriterion(HibernateCriteriaQueryUtilities.getEqualRestriction(
                HibernateCriteriaQueryUtilities.getIdentifierParameter(procAlias), procedureIdentifier));
        queryObject.setAliases(aliases);
        List<ObservationConstellationOfferingObservationType> obsConstOffObsTypes =
                HibernateCriteriaQueryUtilities.getObservationConstellationOfferingObservationType(queryObject,
                        session);
        for (ObservationConstellationOfferingObservationType obsConstOffObsType : obsConstOffObsTypes) {
            obsConstOffObsType.setDeleted(true);
            session.saveOrUpdate(obsConstOffObsType);
            session.flush();
        }
    }

    private void setObservationsAsDeletedForProcedure(String procedureIdentifier, Session session) {
        HibernateQueryObject queryObject = new HibernateQueryObject();
        Map<String, String> aliases = new HashMap<String, String>(0);
        String obsConstAlias = HibernateCriteriaQueryUtilities.addObservationConstallationAliasToMap(aliases, null);
        String procAlias = HibernateCriteriaQueryUtilities.addProcedureAliasToMap(aliases, obsConstAlias);
        queryObject.addCriterion(HibernateCriteriaQueryUtilities.getEqualRestriction(
                HibernateCriteriaQueryUtilities.getIdentifierParameter(procAlias), procedureIdentifier));
        queryObject.setAliases(aliases);
        List<Observation> observations = HibernateCriteriaQueryUtilities.getObservations(queryObject, session);
        for (Observation observation : observations) {
            observation.setDeleted(true);
            session.saveOrUpdate(observation);
            session.flush();
        }
    }
}
