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
package org.n52.sos.service.admin.request.operator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.sos.encode.Encoder;
import org.n52.sos.encode.EncoderKey;
import org.n52.sos.encode.XmlEncoderKey;
import org.n52.sos.exception.AdministratorException;
import org.n52.sos.exception.ConfigurationException;
import org.n52.sos.exception.ows.InvalidParameterValueException;
import org.n52.sos.exception.ows.MissingParameterValueException;
import org.n52.sos.exception.ows.OperationNotSupportedException;
import org.n52.sos.exception.ows.concrete.EncoderResponseUnsupportedException;
import org.n52.sos.exception.ows.concrete.ErrorWhileSavingResponseToOutputStreamException;
import org.n52.sos.exception.ows.concrete.NoEncoderForResponseException;
import org.n52.sos.ogc.ows.CompositeOwsException;
import org.n52.sos.ogc.ows.DCP;
import org.n52.sos.ogc.ows.OWSOperation;
import org.n52.sos.ogc.ows.OWSOperationsMetadata;
import org.n52.sos.ogc.ows.OWSParameterValuePossibleValues;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.ows.SosCapabilities;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.response.GetCapabilitiesResponse;
import org.n52.sos.response.ServiceResponse;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.admin.AdministratorConstants.AdministratorParams;
import org.n52.sos.service.admin.request.AdminRequest;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.XmlOptionsHelper;

public class SosAdminRequestOperator implements AdminRequestOperator {

    /*
     * To support full dynamic loading of a new JAR, the Tomcat context.xml file has to be modified.
     * Add the attribute 'reloadable="true"' to <Context>.
     * Or you have to reload the Webapp.
     * Maybe there are other solution: CLassLoader, ...
     */
    private static final String KEY = "SOS";
    private static final String CONTENT_TYPE_PLAIN = "text/plain";
    public static final String REQUEST_GET_CAPABILITIES = "GetCapabilities";
    public static final String REQUEST_UPDATE = "Update";
    public static final String UPDATE_ENCODER = "Encoder";
    public static final String UPDATE_DECODER = "Decoder";
    public static final String UPDATE_OPERATIONS = "Operations";
    public static final String UPDATE_SERVICES = "Services";
    public static final String UPDATE_BINDINGS = "Bindings";
    public static final String UPDATE_CONFIGURATION = "Configuration";
    private static final List<String> PARAMETERS = CollectionHelper.list(UPDATE_BINDINGS,
                                                                         UPDATE_CONFIGURATION,
                                                                         UPDATE_DECODER,
                                                                         UPDATE_ENCODER,
                                                                         UPDATE_OPERATIONS,
                                                                         UPDATE_SERVICES);

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public ServiceResponse receiveRequest(AdminRequest request) throws AdministratorException, OwsExceptionReport {
        try {
            if (request.getRequest().equalsIgnoreCase(REQUEST_GET_CAPABILITIES)) {
                return createCapabilities();
            } else if (request.getRequest().equalsIgnoreCase(REQUEST_UPDATE)) {
                return handleUpdateRequest(request);
            } else {
                throw new OperationNotSupportedException(request.getRequest());
            }
        } catch (ConfigurationException e) {
            throw new AdministratorException(e);
        }
    }

    private ServiceResponse handleUpdateRequest(AdminRequest request) throws ConfigurationException,
                                                                             OwsExceptionReport {
        String[] parameters = request.getParameters();
        if (parameters != null && parameters.length > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append("The following resources are successful updated: ");
            CompositeOwsException exceptions = new CompositeOwsException();
            for (String parameter : parameters) {
                if (parameter.equalsIgnoreCase(UPDATE_BINDINGS)) {
                    Configurator.getInstance().getBindingRepository().update();
                    builder.append("Bindings");
                } else if (parameter.equalsIgnoreCase(UPDATE_CONFIGURATION)) {
                    Configurator.getInstance().updateConfiguration();
                    builder.append("Configuration");
                } else if (parameter.equalsIgnoreCase(UPDATE_DECODER)) {
                    Configurator.getInstance().getCodingRepository().updateDecoders();
                    builder.append("Decoder");
                } else if (parameter.equalsIgnoreCase(UPDATE_ENCODER)) {
                    Configurator.getInstance().getCodingRepository().updateEncoders();
                    builder.append("Encoder");
                } else if (parameter.equalsIgnoreCase(UPDATE_OPERATIONS)) {
                    Configurator.getInstance().getRequestOperatorRepository().update();
                    builder.append("Supported Operations");
                } else if (parameter.equalsIgnoreCase(UPDATE_SERVICES)) {
                    Configurator.getInstance().getServiceOperatorRepository().update();
                    builder.append("Supported Services");
                } else {
                    exceptions.add(new InvalidParameterValueException(AdministratorParams.parameter, parameter));
                }
                builder.append(", ");
            }
            exceptions.throwIfNotEmpty();
            builder.delete(builder.lastIndexOf(", "), builder.length());
            return createServiceResponse(builder.toString());
        } else {
            throw new MissingParameterValueException(AdministratorParams.parameter);
        }
    }

    private ServiceResponse createCapabilities() throws OwsExceptionReport {
        GetCapabilitiesResponse response = new GetCapabilitiesResponse();
        response.setService(SosConstants.SOS);
        SosCapabilities sosCapabilities = new SosCapabilities();
        OWSOperationsMetadata operationsMetadata = new OWSOperationsMetadata();
        List<OWSOperation> opsMetadata = new ArrayList<OWSOperation>(2);
        opsMetadata.add(getOpsMetadataForCapabilities());
        opsMetadata.add(getOpsMetadataForUpdate());
        operationsMetadata.setOperations(opsMetadata);
        operationsMetadata.addCommonValue(AdministratorParams.service.name(), new OWSParameterValuePossibleValues(KEY));
        sosCapabilities.setOperationsMetadata(operationsMetadata);
        response.setCapabilities(sosCapabilities);
        return createServiceResponse(response);
    }

    private OWSOperation getOpsMetadataForCapabilities() {
        OWSOperation opsMeta = new OWSOperation();
        opsMeta.setOperationName(REQUEST_GET_CAPABILITIES);
        opsMeta.setDcp(getDCP());
        opsMeta.addAnyParameterValue(AdministratorParams.parameter);
        return opsMeta;
    }

    private OWSOperation getOpsMetadataForUpdate() {
        OWSOperation opsMeta = new OWSOperation();
        opsMeta.setOperationName(REQUEST_UPDATE);
        opsMeta.setDcp(getDCP());
        opsMeta.addPossibleValuesParameter(AdministratorParams.parameter, PARAMETERS);
        return opsMeta;
    }

    private Map<String, Set<DCP>> getDCP() {
        return Collections.singletonMap(SosConstants.HTTP_GET,
                                        Collections.singleton(new DCP(Configurator.getInstance().getServiceURL() +
                                                                      "/admin?")));
    }

    private ServiceResponse createServiceResponse(String string) throws OwsExceptionReport {
        String contentType = CONTENT_TYPE_PLAIN;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(string.getBytes());
            return new ServiceResponse(baos, contentType, false, true);
        } catch (IOException e) {
            throw new ErrorWhileSavingResponseToOutputStreamException(e);
        }
    }

    private ServiceResponse createServiceResponse(GetCapabilitiesResponse response) throws OwsExceptionReport {
        String contentType = SosConstants.CONTENT_TYPE_XML;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            EncoderKey key = new XmlEncoderKey(Sos2Constants.NS_SOS_20, GetCapabilitiesResponse.class);
            Encoder<?, GetCapabilitiesResponse> encoder = Configurator.getInstance().getCodingRepository()
                    .getEncoder(key);
            if (encoder != null) {
                Object encodedObject = encoder.encode(response);
                if (encodedObject instanceof XmlObject) {
                    ((XmlObject) encodedObject).save(baos, XmlOptionsHelper.getInstance().getXmlOptions());
                    return new ServiceResponse(baos, contentType, false, true);
                } else if (encodedObject instanceof ServiceResponse) {
                    return (ServiceResponse) encodedObject;
                } else {
                    throw new EncoderResponseUnsupportedException();
                }
            } else {
                throw new NoEncoderForResponseException();
            }

        } catch (IOException ioe) {
            throw new ErrorWhileSavingResponseToOutputStreamException(ioe);
        }
    }
}