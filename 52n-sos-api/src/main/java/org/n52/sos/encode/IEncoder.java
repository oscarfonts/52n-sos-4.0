/***************************************************************
 Copyright (C) 2012
 by 52 North Initiative for Geospatial Open Source Software GmbH

 Contact: Andreas Wytzisk
 52 North Initiative for Geospatial Open Source Software GmbH
 Martin-Luther-King-Weg 24
 48155 Muenster, Germany
 info@52north.org

 This program is free software; you can redistribute and/or modify it under 
 the terms of the GNU General Public License version 2 as published by the 
 Free Software Foundation.

 This program is distributed WITHOUT ANY WARRANTY; even without the implied
 WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with
 this program (see gnu-gpl v2.txt). If not, write to the Free Software
 Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 visit the Free Software Foundation web page, http://www.fsf.org.

 Author: <LIST OF AUTHORS/EDITORS>
 Created: <CREATION DATE>
 Modified: <DATE OF LAST MODIFICATION (optional line)>
 ***************************************************************/

package org.n52.sos.encode;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosConstants.HelperValues;
import org.n52.sos.service.IConformanceClass;
import org.n52.sos.service.ServiceConstants.SupportedTypeKey;

public interface IEncoder<S, T> extends IConformanceClass {

    /**
     * @return
     */
    public List<EncoderKeyType> getEncoderKeyType();

    /**
     * @param element1
     * @return
     * @throws OwsExceptionReport
     */
    public S encode(T element1) throws OwsExceptionReport;

    /**
     * @param element
     * @param additionalValues
     * @return
     * @throws OwsExceptionReport
     */
    public S encode(T element, Map<HelperValues, String> additionalValues) throws OwsExceptionReport;
    
    public Map<SupportedTypeKey, Set<String>> getSupportedTypes();
    
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap);
    
}
