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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.n52.sos.ds.AbstractDescribeSensorDAO;
import org.n52.sos.ds.hibernate.entities.FeatureOfInterest;
import org.n52.sos.ds.hibernate.entities.Observation;
import org.n52.sos.ds.hibernate.entities.Procedure;
import org.n52.sos.ds.hibernate.util.HibernateCriteriaQueryUtilities;
import org.n52.sos.ds.hibernate.util.HibernateProcedureUtilities;
import org.n52.sos.exception.ows.NoApplicableCodeException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.response.DescribeSensorResponse;
import org.n52.sos.util.SosHelper;

/**
 * Implementation of the interface IDescribeSensorDAO
 * 
 */
public class DescribeSensorDAO extends AbstractDescribeSensorDAO {
    private HibernateSessionHolder sessionHolder = new HibernateSessionHolder();

    @Override
    public DescribeSensorResponse getSensorDescription(DescribeSensorRequest request) throws OwsExceptionReport {
        // sensorDocument which should be returned
        Session session = null;
        try {
            session = sessionHolder.getSession();
            SosProcedureDescription result = queryProcedure(request, session);
            
            Collection<String> features = getFeatureOfInterestIDsForProcedure(request.getProcedure(), request.getVersion(), session);
            if (features != null && !features.isEmpty()) {
                result.addFeatureOfInterest(new HashSet<String>(features), request.getProcedure());
            }

            // parent procs
            Collection<String> parentProcedures = getParentProcedures(request.getProcedure(), request.getVersion());
            if (parentProcedures != null && !parentProcedures.isEmpty()) {
                result.addParentProcedures(new HashSet<String>(parentProcedures), request.getProcedure());
            }

            // child procs
            Set<SosProcedureDescription> childProcedures =
                    getChildProcedures(request.getProcedure(), request.getProcedureDescriptionFormat(),
                            request.getVersion(), session);
            if (childProcedures != null && !childProcedures.isEmpty()) {
                result.addChildProcedures(childProcedures, request.getProcedure());
            }
            DescribeSensorResponse response = new DescribeSensorResponse();
            response.setService(request.getService());
            response.setVersion(request.getVersion());
            response.setOutputFormat(request.getProcedureDescriptionFormat());
            response.setSensorDescription(result);
            return response;
        } catch (HibernateException he) {
            throw new NoApplicableCodeException().causedBy(he)
                    .withMessage("Error while querying data for DescribeSensor document!");
        } finally {
            sessionHolder.returnSession(session);
        }
    }

    private SosProcedureDescription queryProcedure(DescribeSensorRequest request, Session session)
            throws OwsExceptionReport {
        Procedure procedure = HibernateCriteriaQueryUtilities.getProcedureForIdentifier(request.getProcedure(), session);
        return HibernateProcedureUtilities.createSosProcedureDescription(procedure, request.getProcedure(), request.getProcedureDescriptionFormat());
        
       
    }

    @SuppressWarnings("unchecked")
    private Collection<String> getFeatureOfInterestIDsForProcedure(String procedure, String version,
                                                                   Session session) throws OwsExceptionReport {
        Criteria c = session.createCriteria(Observation.class);
        c.createCriteria(Observation.PROCEDURE).add(Restrictions.eq(Procedure.IDENTIFIER, procedure));
        c.createCriteria(Observation.FEATURE_OF_INTEREST)
                .setProjection(Projections.distinct(Projections.property(FeatureOfInterest.IDENTIFIER)));
        // FIXME: checks for generated IDs and remove them for SOS 2.0
        return SosHelper.getFeatureIDs(c.list(), version);
    }

    /**
     * Add parent procedures to a SystemDocument
     * 
     * @param xb_systemDoc
     *            System document to add parent procedures to
     * @param parentProcedureIds
     *            The parent procedures to add

     *
     * @throws OwsExceptionReport
     */
    private Set<String> getParentProcedures(String procID, String version) throws OwsExceptionReport {
        return getCache().getParentProcedures(procID, false, false);
        // if (parentProcedureIds != null && !parentProcedureIds.isEmpty()) {
        // SosSMLCapabilities capabilities = new SosSMLCapabilities();
        // capabilities.setName(SosConstants.SYS_CAP_PARENT_PROCEDURES_NAME);
        // String urlPattern =
        // SosHelper.getUrlPatternForHttpGetMethod(new
        // OperationDecoderKey(SosConstants.SOS, version,
        // SosConstants.Operations.DescribeSensor.name()));
        // for (String parentProcID : parentProcedureIds) {
        // SosGmlMetaDataProperty metadata = new SosGmlMetaDataProperty();
        // metadata.setTitle(parentProcID);
        // try {
        // metadata.setHref(SosHelper.getDescribeSensorUrl(version,
        // getConfigurator().getServiceURL(),
        // parentProcID, urlPattern));
        // } catch (UnsupportedEncodingException uee) {
        // String exceptionText = "Error while encoding DescribeSensor URL";
        // LOGGER.debug(exceptionText);
        // throw Util4Exceptions.createNoApplicableCodeException(uee,
        // exceptionText);
        // }
        // capabilities.addMetaDataProperties(metadata);
        // }
        // capabilities.setDataRecord(new SosSweSimpleDataRecord());
        // return capabilities;
        // }
        // return null;
    }

    /**
     * Add a collection of child procedures to a SystemDocument
     * 
     * @param xb_systemDoc
     *            System document to add child procedures to
     * @param childProcedures
     *            The child procedures to add

     *
     * @throws OwsExceptionReport
     */
    private Set<SosProcedureDescription> getChildProcedures(String procID, String outputFormat, String version,
                                                            Session session) throws OwsExceptionReport {
        Set<SosProcedureDescription> childProcedures = new HashSet<SosProcedureDescription>(0);
        Collection<String> childProcedureIds = getCache().getChildProcedures(procID, false, false);

        if (childProcedureIds != null && !childProcedureIds.isEmpty()) {
//            String urlPattern =
//                    SosHelper.getUrlPatternForHttpGetMethod(new OperationDecoderKey(SosConstants.SOS, version,
//                            SosConstants.Operations.DescribeSensor.name()));
            for (String childProcID : childProcedureIds) {
                Procedure procedure = HibernateCriteriaQueryUtilities.getProcedureForIdentifier(childProcID, session);
                childProcedures.add(HibernateProcedureUtilities.createSosProcedureDescription(procedure, childProcID, outputFormat));

                // int childCount = 0;
                // childCount++;
                // SosSMLComponent component = new SosSMLComponent("component" +
                // childCount);
                // component.setTitle(childProcID);
                // if
                // (getConfigurator().isChildProceduresEncodedInParentsDescribeSensor())
                // {
                //
                // } else {
                // try {
                // component.setHref(SosHelper.getDescribeSensorUrl(Sos2Constants.SERVICEVERSION,
                // getConfigurator().getServiceURL(), childProcID, urlPattern));
                // } catch (UnsupportedEncodingException uee) {
                // String exceptionText =
                // "Error while encoding DescribeSensor URL";
                // LOGGER.debug(exceptionText);
                // throw Util4Exceptions.createNoApplicableCodeException(uee,
                // exceptionText);
                // }
                // }
                // smlComponsents.add(component);
            }
        }
        return childProcedures;
    }
}
