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
package org.n52.sos.exception.ows;

import static org.n52.sos.util.HTTPConstants.StatusCode.BAD_REQUEST;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * 
 * @since 4.0.0
 */
public class InvalidParameterValueException extends CodedOwsException {
    private static final long serialVersionUID = 7664405001972222761L;

    public InvalidParameterValueException() {
        super(OwsExceptionCode.InvalidParameterValue);
        setStatus(BAD_REQUEST);
    }

    public InvalidParameterValueException(final String parameterName, final String value) {
        super(OwsExceptionCode.InvalidParameterValue);
        withMessage("The value '%s' of the parameter '%s' is invalid", value, parameterName).at(parameterName);
        setStatus(BAD_REQUEST);
    }

    public InvalidParameterValueException(final Enum<?> parameterName, final String value) {
        this(parameterName.name(), value);
    }
}