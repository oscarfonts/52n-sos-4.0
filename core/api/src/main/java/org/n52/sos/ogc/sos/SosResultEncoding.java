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
package org.n52.sos.ogc.sos;


import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject.Factory;
import org.n52.sos.exception.ows.concrete.DecoderResponseUnsupportedException;
import org.n52.sos.exception.ows.concrete.XmlDecodingException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.SosHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SosResultEncoding {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SosHelper.class);
    
    private String xml;

    private SweAbstractEncoding encoding;
    
    public SosResultEncoding() {
    }

    public SosResultEncoding(String resultEncoding) throws OwsExceptionReport {
        this.xml = resultEncoding;
        encoding = parseResultEncoding();
    }

    public String getXml() {
        if (encoding != null) {
            return encoding.getXml();
        }
        return xml;
    }

    public void setEncoding(SweAbstractEncoding encoding) {
        this.encoding = encoding;
    }

    public SweAbstractEncoding getEncoding() throws OwsExceptionReport {
        if (encoding == null && xml != null && !xml.isEmpty()) {
            encoding = parseResultEncoding();
        }
       return encoding;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
    
    private SweAbstractEncoding parseResultEncoding() throws OwsExceptionReport {
        try {
            Object decodedObject = CodingHelper.decodeXmlObject(Factory.parse(xml));
            if (decodedObject instanceof SweAbstractEncoding) {
                return (SweAbstractEncoding) decodedObject;
            } else {
                throw new DecoderResponseUnsupportedException(xml, decodedObject);
            }
        } catch (XmlException xmle) {
            throw new XmlDecodingException("resultEncoding", xml, xmle);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SosResultEncoding other = (SosResultEncoding) obj;
        try {
            if (this.getEncoding() != other.getEncoding() && (this.getEncoding() == null 
                    || !this.getEncoding().equals(other.getEncoding()))) {
                return false;
            }
        } catch (OwsExceptionReport ex) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        try {
            return getEncoding().hashCode();
        } catch (OwsExceptionReport e) {
            LOGGER.error("Error while parsing resultStructure", e);
        }
        return super.hashCode();
    }

}
