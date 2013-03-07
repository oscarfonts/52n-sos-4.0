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

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.n52.sos.ds.AbstractGetResultTemplateDAO;
import org.n52.sos.ds.hibernate.entities.ResultTemplate;
import org.n52.sos.ds.hibernate.util.HibernateCriteriaQueryUtilities;
import org.n52.sos.ds.hibernate.util.ResultHandlingHelper;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.request.GetResultTemplateRequest;
import org.n52.sos.response.GetResultTemplateResponse;
import org.n52.sos.util.Util4Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetResultTemplateDAO extends AbstractGetResultTemplateDAO {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertResultDAO.class);
    
    private HibernateSessionHolder sessionHolder = new HibernateSessionHolder();

    @Override
    public GetResultTemplateResponse getResultTemplate(GetResultTemplateRequest request) throws OwsExceptionReport {
        Session session = null;
        try {
            session = sessionHolder.getSession();
            ResultTemplate resultTemplate =
                    HibernateCriteriaQueryUtilities.getResultTemplateObject(request.getOffering(),
                            request.getObservedProperty(), session);
            if (resultTemplate != null) {
                GetResultTemplateResponse response = new GetResultTemplateResponse();
                response.setService(request.getService());
                response.setVersion(request.getVersion());
                response.setResultEncoding(ResultHandlingHelper.createSosResultEncoding(resultTemplate.getResultEncoding()));
                response.setResultStructure(ResultHandlingHelper.createSosResultStructure(resultTemplate.getResultStructure()));
                return response;
            }
            StringBuilder exceptionText = new StringBuilder();
            exceptionText.append("For the requested combination offering (");
            exceptionText.append(request.getOffering());
            exceptionText.append(") and observedProperty (");
            exceptionText.append(request.getObservedProperty());
            exceptionText.append(") no SWE Common 2.0 encoded result values are available!");
            throw Util4Exceptions.createInvalidPropertyOfferingCombination(exceptionText.toString());
        } catch (HibernateException he) {
            String exceptionText = "Error while querying data result template data!";
            LOGGER.error(exceptionText, he);
            throw Util4Exceptions.createNoApplicableCodeException(he, exceptionText);
        } finally {
            sessionHolder.returnSession(session);
        }
    }

}
