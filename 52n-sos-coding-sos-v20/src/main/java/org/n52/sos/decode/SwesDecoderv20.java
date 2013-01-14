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
package org.n52.sos.decode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.sos.x20.SosInsertionMetadataPropertyType;
import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.DeleteSensorDocument;
import net.opengis.swes.x20.DescribeSensorDocument;
import net.opengis.swes.x20.DescribeSensorType;
import net.opengis.swes.x20.InsertSensorDocument;
import net.opengis.swes.x20.InsertSensorType;
import net.opengis.swes.x20.InsertSensorType.Metadata;
import net.opengis.swes.x20.InsertSensorType.RelatedFeature;
import net.opengis.swes.x20.UpdateSensorDescriptionDocument;
import net.opengis.swes.x20.UpdateSensorDescriptionType;
import net.opengis.swes.x20.UpdateSensorDescriptionType.Description;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.sos.ogc.OGCConstants;
import org.n52.sos.ogc.gml.CodeWithAuthority;
import org.n52.sos.ogc.om.features.samplingFeatures.SosSamplingFeature;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.ogc.sos.SosProcedureDescription;
import org.n52.sos.ogc.swe.SWEConstants;
import org.n52.sos.ogc.swe.SosFeatureRelationship;
import org.n52.sos.ogc.swe.SosMetadata;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.request.DeleteSensorRequest;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.request.InsertSensorRequest;
import org.n52.sos.request.UpdateSensorRequest;
import org.n52.sos.service.AbstractServiceCommunicationObject;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.ServiceConstants.SupportedTypeKey;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.CollectionHelper;
import org.n52.sos.util.SosHelper;
import org.n52.sos.util.StringHelper;
import org.n52.sos.util.Util4Exceptions;
import org.n52.sos.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SwesDecoderv20 implements IDecoder<AbstractServiceCommunicationObject, XmlObject> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwesDecoderv20.class);
    
    private Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
        CodingHelper.decoderKeysForElements(SWEConstants.NS_SWES_20,
            DescribeSensorDocument.class,
            InsertSensorDocument.class,
            UpdateSensorDescriptionDocument.class,
            DeleteSensorDocument.class
        ),
        CodingHelper.xmlDecoderKeysForOperation(
            SosConstants.SOS, Sos2Constants.SERVICEVERSION,
            SosConstants.Operations.DescribeSensor,
            Sos2Constants.Operations.InsertSensor,
            Sos2Constants.Operations.UpdateSensorDescription,
            Sos2Constants.Operations.DeleteSensor
        )
    );
    
    public SwesDecoderv20() {
       LOGGER.debug("Decoder for the following keys initialized successfully: {}!", StringHelper.join(", ", DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getDecoderKeyTypes() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Map<SupportedTypeKey, Set<String>> getSupportedTypes() {
        return Collections.emptyMap();
    }
    
    @Override
    public Set<String> getConformanceClasses() {
        return Collections.emptySet();
    }

    @Override
    public AbstractServiceRequest decode(XmlObject xmlObject) throws OwsExceptionReport {
        AbstractServiceRequest request = null;
        LOGGER.debug("REQUESTTYPE:" + xmlObject.getClass());
        // validate document
        XmlHelper.validateDocument(xmlObject);
        if (xmlObject instanceof DescribeSensorDocument) {
            DescribeSensorDocument obsDoc = (DescribeSensorDocument) xmlObject;
            request = parseDescribeSensor(obsDoc);
        } else if (xmlObject instanceof InsertSensorDocument) {
            InsertSensorDocument xbInsSensDoc = (InsertSensorDocument) xmlObject;
            request = parseInsertSensor(xbInsSensDoc);
        } else if (xmlObject instanceof UpdateSensorDescriptionDocument) {
            UpdateSensorDescriptionDocument xbUpSenDoc = (UpdateSensorDescriptionDocument) xmlObject;
            request = parseUpdateSensorDescription(xbUpSenDoc);
        } else if (xmlObject instanceof DeleteSensorDocument) {
            DeleteSensorDocument xbDelSenDoc = (DeleteSensorDocument) xmlObject;
            request = parseDeleteSensor(xbDelSenDoc);
        }
        return request;
    }

    /**
     * parses the passes XmlBeans document and creates a SOS describeSensor
     * request
     * 
     * @param xbDescSenDoc
     *            XmlBeans document representing the describeSensor request
     * @return Returns SOS describeSensor request
     * @throws OwsExceptionReport
     *             if validation of the request failed
     */
    private AbstractServiceRequest parseDescribeSensor(DescribeSensorDocument xbDescSenDoc) throws OwsExceptionReport {
        DescribeSensorRequest descSensorRequest = new DescribeSensorRequest();
        DescribeSensorType xbDescSensor = xbDescSenDoc.getDescribeSensor();
        descSensorRequest.setService(xbDescSensor.getService());
        descSensorRequest.setVersion(xbDescSensor.getVersion());
        descSensorRequest.setProcedures(xbDescSensor.getProcedure());
        if (xbDescSensor.getProcedureDescriptionFormat() != null
                && !xbDescSensor.getProcedureDescriptionFormat().isEmpty()) {
            descSensorRequest.setProcedureDescriptionFormat(xbDescSensor.getProcedureDescriptionFormat());
        } else {
            descSensorRequest.setProcedureDescriptionFormat(SosConstants.PARAMETER_NOT_SET);
        }
        return descSensorRequest;
    }

    private AbstractServiceRequest parseInsertSensor(InsertSensorDocument xbInsSensDoc) throws OwsExceptionReport {
        InsertSensorRequest request = new InsertSensorRequest();
        InsertSensorType xbInsertSensor = xbInsSensDoc.getInsertSensor();
        request.setService(xbInsertSensor.getService());
        request.setVersion(xbInsertSensor.getVersion());
        // format
        request.setProcedureDescriptionFormat(xbInsertSensor.getProcedureDescriptionFormat());
        // observable properties
        if (xbInsertSensor.getObservablePropertyArray() != null
                && xbInsertSensor.getObservablePropertyArray().length > 0) {
            request.setObservableProperty(Arrays.asList(xbInsertSensor.getObservablePropertyArray()));
        }
        // related features
        request.setRelatedFeature(parseRelatedFeature(xbInsertSensor.getRelatedFeatureArray()));
        // metadata
        if (xbInsertSensor.getMetadataArray() != null && xbInsertSensor.getMetadataArray().length > 0) {
            request.setMetadata(parseMetadata(xbInsertSensor.getMetadataArray()));
        }
        SosHelper.checkProcedureDescriptionFormat(xbInsertSensor.getProcedureDescriptionFormat(), 
                Sos2Constants.InsertSensorParams.procedureDescriptionFormat.name());
        
        try {
            XmlObject xmlObject =
                    XmlObject.Factory.parse(getNodeFromNodeList(xbInsertSensor.getProcedureDescription()
                            .getDomNode().getChildNodes()));
            
            IDecoder<?, XmlObject> decoder = Configurator.getInstance().getCodingRepository()
                    .getDecoder(CodingHelper.getDecoderKey(xmlObject));
            if (decoder == null) {
                 String exceptionText = "The requested procedureDescritpionFormat is not supported!";
                LOGGER.error(exceptionText);
                throw Util4Exceptions.createInvalidParameterValueException(
                        Sos2Constants.InsertSensorParams.procedureDescriptionFormat.name(), exceptionText);
            }
            Object decodedObject = decoder.decode(xmlObject);
            if (decodedObject != null && decodedObject instanceof SosProcedureDescription) {
                request.setProcedureDescription((SosProcedureDescription) decodedObject);
            }
        } catch (XmlException xmle) {
            String exceptionText = "Error while parsing procedure description of InsertSensor request!";
            LOGGER.error(exceptionText, xmle);
            throw Util4Exceptions.createNoApplicableCodeException(xmle, exceptionText);
        }
        return request;
    }

    private AbstractServiceRequest parseDeleteSensor(DeleteSensorDocument xbDelSenDoc) {
        DeleteSensorRequest request = new DeleteSensorRequest();
        request.setService(xbDelSenDoc.getDeleteSensor().getService());
        request.setVersion(xbDelSenDoc.getDeleteSensor().getVersion());
        request.setProcedureIdentifier(xbDelSenDoc.getDeleteSensor().getProcedure());
        return request;
    }

    /**
     * parses the Xmlbeans UpdateSensorDescription document to a SOS request.
     * 
     * @param xbUpSenDoc
     *            UpdateSensorDescription document
     * @return SOS UpdateSensor request
     * @throws OwsExceptionReport
     *             if an error occurs.
     */
    private AbstractServiceRequest parseUpdateSensorDescription(UpdateSensorDescriptionDocument xbUpSenDoc)
            throws OwsExceptionReport {
        UpdateSensorRequest request = new UpdateSensorRequest();
        UpdateSensorDescriptionType xbUpdateSensor = xbUpSenDoc.getUpdateSensorDescription();
        request.setService(xbUpdateSensor.getService());
        request.setVersion(xbUpdateSensor.getVersion());
        request.setProcedureIdentifier(xbUpdateSensor.getProcedure());
        request.setProcedureDescriptionFormat(xbUpdateSensor.getProcedureDescriptionFormat());
        
        
        for (Description description : xbUpdateSensor.getDescriptionArray()) {
            try {
                XmlObject xmlObject =
                        XmlObject.Factory.parse(getNodeFromNodeList(description.getSensorDescription()
                                .getData().getDomNode().getChildNodes()));
                IDecoder<?, XmlObject> decoder = Configurator.getInstance().getCodingRepository()
                        .getDecoder(CodingHelper.getDecoderKey(xmlObject));
                if (decoder == null) {
                    String exceptionText = "The requested procedureDescritpionFormat is not supported!";
                    LOGGER.error(exceptionText);
                    throw Util4Exceptions.createInvalidParameterValueException(
                            Sos2Constants.UpdateSensorDescriptionParams.procedureDescriptionFormat.name(), exceptionText);
                }
                
                Object decodedObject = decoder.decode(xmlObject);
                if (decodedObject != null && decodedObject instanceof SosProcedureDescription) {
                    request.addProcedureDescriptionString((SosProcedureDescription) decodedObject);
                }
            } catch (XmlException xmle) {
                String exceptionText = "Error while parsing procedure description of UpdateSensor request!";
                LOGGER.error(exceptionText, xmle);
                throw Util4Exceptions.createNoApplicableCodeException(xmle, exceptionText);
            }
        }
        return request;
    }

    private SosMetadata parseMetadata(Metadata[] metadataArray) throws OwsExceptionReport {

        SosMetadata sosMetadata = new SosMetadata();
        try {
            for (Metadata metadata : metadataArray) {
                SosInsertionMetadataType xbSosInsertionMetadata = null;
                if (metadata.getInsertionMetadata() != null
                        && metadata.getInsertionMetadata().schemaType() == SosInsertionMetadataType.type) {
                    xbSosInsertionMetadata = (SosInsertionMetadataType) metadata.getInsertionMetadata();
                } else {
                    if (metadata.getDomNode().hasChildNodes()) {
                        Node node = getNodeFromNodeList(metadata.getDomNode().getChildNodes());
                        SosInsertionMetadataPropertyType xbMetadata =
                                SosInsertionMetadataPropertyType.Factory.parse(node);
                        xbSosInsertionMetadata = xbMetadata.getSosInsertionMetadata();
                    }
                }
                if (xbSosInsertionMetadata != null) {
                    // featureOfInterest types
                    if (xbSosInsertionMetadata.getFeatureOfInterestTypeArray() != null) {
                        sosMetadata.setFeatureOfInterestTypes(Arrays.asList(xbSosInsertionMetadata
                                .getFeatureOfInterestTypeArray()));
                    }
                    // observation types
                    if (xbSosInsertionMetadata.getObservationTypeArray() != null) {
                        sosMetadata
                                .setObservationTypes(Arrays.asList(xbSosInsertionMetadata.getObservationTypeArray()));
                    }
                }
            }
        } catch (XmlException xmle) {
            String exceptionText = "An error occurred while parsing the metadata in the http post request";
            LOGGER.error(exceptionText, xmle);
            throw Util4Exceptions.createNoApplicableCodeException(xmle, exceptionText);
        }
        return sosMetadata;
    }

    private List<SosFeatureRelationship> parseRelatedFeature(RelatedFeature[] relatedFeatureArray) {
        List<SosFeatureRelationship> sosRelatedFeatures = new ArrayList<SosFeatureRelationship>(relatedFeatureArray.length);
        for (RelatedFeature relatedFeature : relatedFeatureArray) {
            SosFeatureRelationship sosFeatureRelationship = new SosFeatureRelationship();

            FeaturePropertyType fpt = relatedFeature.getFeatureRelationship().getTarget();
            if (fpt.getHref() != null && !fpt.getHref().isEmpty()) {
                String identifier;
                if (fpt.getTitle() != null && !fpt.getTitle().isEmpty()) {
                    identifier = fpt.getTitle();
                } else {
                    identifier = fpt.getHref();
                }
                SosSamplingFeature feature = new SosSamplingFeature(new CodeWithAuthority(identifier));
                if (checkForRequestUrl(fpt.getHref())) {
                    feature.setUrl(fpt.getHref());
                }
                feature.setFeatureType(OGCConstants.UNKNOWN);
                sosFeatureRelationship.setFeature(feature);
            } else {
                // TODO: decode encoded feature, XML-Represent
            }
            sosFeatureRelationship.setRole(relatedFeature.getFeatureRelationship().getRole());
            sosRelatedFeatures.add(sosFeatureRelationship);
        }
        return sosRelatedFeatures;
    }

    private boolean checkForRequestUrl(String href) {
        return href.toLowerCase().contains("request=");
    }

    private Node getNodeFromNodeList(NodeList nodeList) {
        if (nodeList != null && nodeList.getLength() > 0) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    return nodeList.item(i);
                }
            }
        }
        return null;
    }
}
