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
package org.n52.sos.ogc.ows;

import javax.xml.namespace.QName;

import org.n52.sos.exception.ows.OwsExceptionCode;
import org.n52.sos.util.SchemaLocation;

/**
 * Constants for OWS.
 */
public interface OWSConstants {

    // namespace and schema locations
    String NS_OWS = "http://www.opengis.net/ows/1.1";

    String NS_OWS_PREFIX = "ows";

    String SCHEMA_LOCATION_URL_OWS = "http://schemas.opengis.net/ows/1.1.0/owsAll.xsd";

    String SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT =
            "http://schemas.opengis.net/ows/1.1.0/owsExceptionReport.xsd";
    
    SchemaLocation OWS_110_SCHEMA_LOCATION = new SchemaLocation(NS_OWS, SCHEMA_LOCATION_URL_OWS);
    
    SchemaLocation OWS_110_EXCEPTION_REPORT_SCHEMA_LOCATION = new SchemaLocation(NS_OWS_PREFIX, SCHEMA_LOCATION_URL_OWS_EXCEPTIONREPORT);

    // exception messages
    String SOAP_REASON_INVALID_PARAMETER_VALUE =
                               "The request contained an invalid parameter value.";

    String SOAP_REASON_INVALID_UPDATE_SEQUENCES =
                               "The value of the updateSequence parameter in the GetCapabilities operation request was greater than the current value of the service metadata updateSequence number.";

    String SOAP_REASON_MISSING_PARAMETER_VALUE =
                               "The request did not include a value for a required parameter and this server does not declare a default value for it.";

    String SOAP_REASON_NO_APPLICABLE_CODE = "A server exception was encountered.";

    String SOAP_REASON_NO_DATA_AVAILABLE = "There are no data available.";

    String SOAP_REASON_OPERATION_NOT_SUPPORTED =
                               "The requested operation is not supported by this server.";

    String SOAP_REASON_OPTION_NOT_SUPPORTED =
                               "The request included/targeted an option that is not supported by this server.";

    String SOAP_REASON_REQUEST_EXTENSION_NOT_SUPPORTED =
                               "The request included an extension that is not supported by this server.";

    String SOAP_REASON_VERSION_NEGOTIATION_FAILED =
                               "The list of versions in the ‘AcceptVersions’ parameter value of the GetCapabilities operation request did not include any version supported by this server.";

    String SOAP_REASON_RESPONSE_EXCEEDS_SIZE_LIMIT =
                               "The requested result set exceeds the response size limit of this service and thus cannot be delivered.";

    String SOAP_REASON_INVALID_PROPERTY_OFFERING_COMBINATION =
                               "Observations for the requested combination of observedProperty and offering do not use SWE Common encoded results.";

    String SOAP_REASON_UNKNOWN = "A server exception was encountered.";

    String EN_EXCEPTION = "Exception";

    String EN_EXCEPTION_CODE = "exceptionCode";

    String EN_LOCATOR = "locator";

    String EN_EXCEPTION_TEXT = "ExceptionText";
    
    QName QN_EXCEPTION = new QName(NS_OWS, EN_EXCEPTION, NS_OWS_PREFIX);
    QName QN_EXCEPTION_TEXT = new QName(NS_OWS, EN_EXCEPTION_TEXT, NS_OWS_PREFIX);
    QName QN_NO_APPLICABLE_CODE = new QName(NS_OWS, OwsExceptionCode.NoApplicableCode.name(), NS_OWS_PREFIX);

    /**
     * Enumeration for related feature role
     * 
     */
    enum RelatedFeatureRole {
        featureOfInterestID, relatedFeatureID
    }

    /** enum with names of get request parameters for all requests */
    enum RequestParams {
        request, service, version;

        /**
         * method checks whether the string parameter is contained in this
         * enumeration
         * 
         * @param s
         *            the name which should be checked
         * @return true if the name is contained in the enumeration
         */
        public static boolean contains(String s) {
            for (Enum<?> p : values()) {
                if (p.name().equals(s)) {
                    return true;
                }
            }
            return false;
        }
    }
}
