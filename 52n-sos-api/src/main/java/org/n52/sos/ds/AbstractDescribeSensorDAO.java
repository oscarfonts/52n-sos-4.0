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
package org.n52.sos.ds;

import org.n52.sos.ogc.ows.OWSOperation;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sensorML.SensorMLConstants;
import org.n52.sos.ogc.sos.Sos1Constants;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.request.DescribeSensorRequest;
import org.n52.sos.response.DescribeSensorResponse;

/**
 * interface for getting procedure description for a passed DescribeSensor
 * request from the data source
 * 
 */
public abstract class AbstractDescribeSensorDAO extends AbstractOperationDAO {
    
    /**
     * supported SOS operation
     */
    private static final String OPERATION_NAME = SosConstants.Operations.DescribeSensor.name();

    /*
     * (non-Javadoc)
     * 
     * @see org.n52.sos.ds.ISosOperationDAO#getOperationName()
     */
    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    @Override
    protected void setOperationsMetadata(OWSOperation opsMeta, String service, String version)
            throws OwsExceptionReport {
        opsMeta.addPossibleValuesParameter(SosConstants.GetObservationParams.procedure, getCache().getProcedures());
        // FIXME: getTypes from Decoder
        if (version.equals(Sos1Constants.SERVICEVERSION)) {
            opsMeta.addPossibleValuesParameter(Sos1Constants.DescribeSensorParams.outputFormat,
                    SensorMLConstants.SENSORML_OUTPUT_FORMAT_MIME_TYPE);
        } else if (version.equals(Sos2Constants.SERVICEVERSION)) {
            opsMeta.addPossibleValuesParameter(Sos2Constants.DescribeSensorParams.procedureDescriptionFormat,
                    SensorMLConstants.SENSORML_OUTPUT_FORMAT_URL);
        }
    }
    
    /**
     * Get the procedure description for a procedure
     * 
     * @return Returns the DescribeSensor response
     * @throws OwsExceptionReport
     *             If an error occurs
     */
    public abstract DescribeSensorResponse getSensorDescription(DescribeSensorRequest request) throws OwsExceptionReport;

}