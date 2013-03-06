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
package org.n52.sos.request.operator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.ds.IDeleteSensorDAO;
import org.n52.sos.encode.IEncoder;
import org.n52.sos.event.SosEventBus;
import org.n52.sos.event.events.SensorDeletion;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.ConformanceClasses;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.swe.SWEConstants;
import org.n52.sos.request.DeleteSensorRequest;
import org.n52.sos.response.DeleteSensorResponse;
import org.n52.sos.response.ServiceResponse;
import org.n52.sos.service.Configurator;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.Util4Exceptions;
import org.n52.sos.util.XmlOptionsHelper;
import org.n52.sos.wsdl.WSDLOperation;
import org.n52.sos.wsdl.WSDLConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosDeleteSensorOperatorV20 extends AbstractV2RequestOperator<IDeleteSensorDAO, DeleteSensorRequest> {

    private static final String OPERATION_NAME = Sos2Constants.Operations.DeleteSensor.name();
    private static final Set<String> CONFORMANCE_CLASSES = Collections.singleton(ConformanceClasses.SOS_V2_SENSOR_DELETION);
    private static final Logger LOGGER = LoggerFactory.getLogger(SosDeleteSensorOperatorV20.class);

    public SosDeleteSensorOperatorV20() {
        super(OPERATION_NAME, DeleteSensorRequest.class);
    }

    @Override
    public Set<String> getConformanceClasses() {
        return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
    }

    @Override
    public ServiceResponse receive(DeleteSensorRequest request) throws OwsExceptionReport {
        checkRequestedParameter(request);
        DeleteSensorResponse response = getDao().deleteSensor(request);
        SosEventBus.fire(new SensorDeletion(request, response));
        String contentType = SosConstants.CONTENT_TYPE_XML;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            IEncoder<?, DeleteSensorResponse> encoder = Configurator.getInstance().getCodingRepository()
                    .getEncoder(CodingHelper.getEncoderKey(SWEConstants.NS_SWES_20, response));
            if (encoder != null) {
                // TODO valid response object
                Object encodedObject = encoder.encode(response);
                if (encodedObject instanceof XmlObject) {
                    ((XmlObject) encodedObject).save(baos, XmlOptionsHelper.getInstance().getXmlOptions());
                    return new ServiceResponse(baos, contentType, false, true);
                } else if (encodedObject instanceof ServiceResponse) {
                    return (ServiceResponse) encodedObject;
                } else {
                    String exceptionText = "The encoder response is not supported!";
                    throw Util4Exceptions.createNoApplicableCodeException(null, exceptionText);
                }
            } else {
                String exceptionText =
                        "The DeleteSensor operation is not supported!";
                throw Util4Exceptions.createNoApplicableCodeException(null, exceptionText);
            }
        } catch (IOException ioe) {
            String exceptionText = "Error occurs while saving response to output stream!";
            LOGGER.error(exceptionText, ioe);
            throw Util4Exceptions.createNoApplicableCodeException(ioe, exceptionText);
        }
    }

    private void checkRequestedParameter(DeleteSensorRequest request) throws OwsExceptionReport {
        List<OwsExceptionReport> exceptions = new LinkedList<OwsExceptionReport>();
        try {
            checkServiceParameter(request.getService());
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkSingleVersionParameter(request);
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        try {
            checkProcedureIdentifier(request.getProcedureIdentifier());
        } catch (OwsExceptionReport owse) {
            exceptions.add(owse);
        }
        Util4Exceptions.mergeAndThrowExceptions(exceptions);
    }

    private void checkProcedureIdentifier(String procedureIdentifier) throws OwsExceptionReport {
        if (procedureIdentifier != null && !procedureIdentifier.isEmpty()) {
            if (!Configurator.getInstance().getCache().getProcedures().contains(procedureIdentifier)){
                String exceptionText = "The requested procedure identifier (" + procedureIdentifier + ") is not provided by this service!";
                throw Util4Exceptions.createInvalidParameterValueException(Sos2Constants.DeleteSensorParams.procedure.name(), exceptionText);
            }
        } else {
            throw Util4Exceptions.createMissingParameterValueException(Sos2Constants.DeleteSensorParams.procedure.name());
        }
    }
    
    @Override
    public WSDLOperation getSosOperationDefinition() {
        return WSDLConstants.Operations.DELETE_SENSOR;
    }
}
