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

package org.n52.sos.service.it.soap.v2;

import org.n52.sos.service.it.AbstractSoapTest;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.n52.sos.service.it.AbstractSoapTest.invalidServiceParameterValueExceptionFault;

import net.opengis.swes.x20.DescribeSensorDocument;
import net.opengis.swes.x20.DescribeSensorType;

import org.apache.xmlbeans.XmlException;
import org.junit.Test;
import org.n52.sos.ogc.sos.Sos2Constants;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Test for SOAP SOS 2.0 DescribeSensor request.
 * 
 * @author Christian Autermann <c.autermann@52north.org>
 * @author Carsten Hollmann <c.hollmann@52north.org>
 * @since 4.0.0
 */
public class DescribeSensorTest extends AbstractSosV2SoapTest {

    @Override
    @Test
    public void missingServiceParameter() throws XmlException {
        DescribeSensorDocument describeSensorDocument = getRequest("procedure", "procedureDescriptionFormat");
        addVersionParameter(describeSensorDocument.getDescribeSensor());
        missingServiceParameter(describeSensorDocument.getDescribeSensor(), describeSensorDocument);
    }

    @Override
    @Test
    public void emptyServiceParameter() throws XmlException {
        DescribeSensorDocument describeSensorDocument = getRequest("procedure", "procedureDescriptionFormat");
        addVersionParameter(describeSensorDocument.getDescribeSensor());
        emptyServiceParameter(describeSensorDocument.getDescribeSensor(), describeSensorDocument);
    }

    @Override
    @Test
    public void invalidServiceParameter() throws XmlException {
        DescribeSensorDocument describeSensorDocument = getRequest("procedure", "procedureDescriptionFormat");
        addVersionParameter(describeSensorDocument.getDescribeSensor());
        invalidServiceParameter(describeSensorDocument.getDescribeSensor(), describeSensorDocument);
    }

    protected DescribeSensorDocument getRequest(String procedure, String procedureDescriptionFormat) {
        DescribeSensorDocument describeSensorDocument = DescribeSensorDocument.Factory.newInstance();
        DescribeSensorType describeSensorType = describeSensorDocument.addNewDescribeSensor();
        describeSensorType.setProcedure("procedure");
        describeSensorType.setProcedureDescriptionFormat("procedureDescriptionFormat");
        return describeSensorDocument;
    }
}
