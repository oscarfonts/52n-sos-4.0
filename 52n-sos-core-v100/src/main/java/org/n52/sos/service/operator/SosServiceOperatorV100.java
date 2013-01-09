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
package org.n52.sos.service.operator;

import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.Sos1Constants;
import org.n52.sos.ogc.sos.SosConstants;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.request.operator.IRequestOperator;
import org.n52.sos.response.ServiceResponse;
import org.n52.sos.service.Configurator;
import org.n52.sos.util.Util4Exceptions;

public class SosServiceOperatorV100 implements IServiceOperator {

    private ServiceOperatorKeyType serviceOperatorKeyType;

    public SosServiceOperatorV100() {
        serviceOperatorKeyType = new ServiceOperatorKeyType(SosConstants.SOS, Sos1Constants.SERVICEVERSION);
    }

    @Override
    public ServiceResponse receiveRequest(AbstractServiceRequest request) throws OwsExceptionReport {
        ServiceResponse response = null;
        IRequestOperator requestOperator =
                Configurator.getInstance().getRequestOperator(serviceOperatorKeyType, request.getOperationName());
        if (requestOperator != null) {
            response = requestOperator.receiveRequest(request);
        }
        if (response != null) {
            return response;
        }
        throw Util4Exceptions.createOperationNotSupportedException(request.getOperationName());
    }

    @Override
    public ServiceOperatorKeyType getServiceOperatorKeyType() {
        return serviceOperatorKeyType;
    }

}
